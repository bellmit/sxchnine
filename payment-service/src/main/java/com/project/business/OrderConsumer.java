package com.project.business;

import com.project.exception.PaymentMethodException;
import com.project.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.project.utils.PaymentStatusCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderConsumer {

    private final CatchupOrder catchupOrder;
    private final Map<String, Order> nackOrders = new ConcurrentHashMap<>();
    private final Map<String, Order> nackDLTOrders = new ConcurrentHashMap<>();
    private final Map<String, Order> dltCheckoutOrders = new ConcurrentHashMap<>();
    private final Map<String, Order> dltConfirmOrders = new ConcurrentHashMap<>();

    @KafkaListener(groupId = "${kafka.consumer.groupId}",
            topics = "${kafka.consumer.topic}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumeOrder(Order order, Acknowledgment acknowledgment) {
        log.info("consume order {} with status {} for payment", order.getOrderId(), order.getOrderStatus());
        /*
            Rebalance consumer:
            max.poll.interval.ms = 5min
            heartbeat.interval.ms = 3sec
            session.timeout.ms = 10sec
         */
        if (acknowledgeAlreadyProcessedOrders(order, acknowledgment)) return;

        if (order.getOrderStatus().equalsIgnoreCase(WAITING.getValue())
                && (order.getPaymentStatus().equalsIgnoreCase(CHECKOUT_OP.getValue())
                    || order.getPaymentStatus().equalsIgnoreCase(CALL_PAYMENT_METHOD_TIMEOUT.getValue()))) {

            catchupCheckout(order, false);

        } else if (order.getOrderStatus().equalsIgnoreCase(WAITING.getValue())
                && (order.getPaymentStatus().equalsIgnoreCase(CONFIRM_OP.getValue())
                    || order.getPaymentStatus().equalsIgnoreCase(RETRIEVE_PAYMENT_INTENT_TIMEOUT.getValue()))) {

            confirmCheckout(order, false);

        }

        try {
            acknowledgment.acknowledge();
        } catch (Throwable throwable) {
            nackOrders.put(order.getOrderId(), order);
            log.error("cannot commit offset for order {}, we will add it to nack orders", order.getOrderId(), throwable);
        }


    }

    @KafkaListener(groupId = "${kafka.consumer.dlt.groupId}",
            topics = "${kafka.consumer.dlt.topic}",
            containerFactory = "kafkaDLTListenerContainerFactory")
    public void consumerOrderDLT(Order order, Acknowledgment acknowledgment) {
        log.info("DLT consume order {} with status {} for payment", order.getOrderId(), order.getOrderStatus());
        if (nackDLTOrders.get(order.getOrderId()) != null) {
            acknowledgment.acknowledge();
            nackDLTOrders.remove(order.getOrderId());

        } else {

            if (order.getProcessingStatus().equals(WAITING_TIMEOUT.getValue())
                    && order.getTypeProcessing().equals(CHECKOUT_OP.getValue())) {

                catchupCheckout(order, true);

            } else if (order.getProcessingStatus().equals(WAITING_TIMEOUT.getValue())
                    && order.getTypeProcessing().equals(CONFIRM_OP.getValue())) {

                confirmCheckout(order, true);
            }

            try {
                acknowledgment.acknowledge();
            } catch (Throwable throwable) {
                nackDLTOrders.put(order.getOrderId(), order);
                log.error("cannot commit offset for DLT order {}, we will send it to unack topic", order.getOrderId(), throwable);
            }
        }
    }

    @Scheduled(fixedDelay = 500000L)
    public void reprocessFailedDLTOrders() {
        if (!dltCheckoutOrders.isEmpty()) {
            log.info("scheduler - checkout failed orders - nbr of elements to process {}", dltCheckoutOrders.size());
            dltCheckoutOrders.forEach((k, v) -> log.info(v.toString()));

            dltCheckoutOrders.forEach((orderId, order) -> catchupCheckout(order, true));

            log.info("finish checkout failed orders - nbr of elements left {}", dltCheckoutOrders.size());
        }
        if (!dltConfirmOrders.isEmpty()) {
            log.info("scheduler - confirm failed orders - nbr of elements to process {}", dltConfirmOrders.size());

            dltConfirmOrders.forEach((orderId, order) -> confirmCheckout(order, true));

            log.info("finish confirm failed orders - nbr of elements left {}", dltConfirmOrders.size());
        }
    }

    private void catchupCheckout(Order order, boolean isDLT) {
        log.info("catchup checkout for order {}", order.getOrderId());
        Order processedOrder = catchupOrder.catchUpCheckout(order)
                .block();

        if (processedOrder.getProcessingStatus().equals(WAITING_TIMEOUT.getValue())
                && !isDLT) {
            throw new PaymentMethodException("error occurred during consuming order to checkout payment for order:" + order.getOrderId() + " PaymentIntentId: " + order.getPaymentInfo().getPaymentIntentId());
        } else if (processedOrder.getProcessingStatus().equals(WAITING_TIMEOUT.getValue())
                && isDLT) {
            dltCheckoutOrders.putIfAbsent(order.getOrderId(), order);
            return;
        }

        dltCheckoutOrders.remove(order.getOrderId());
    }

    private void confirmCheckout(Order order, boolean isDlt) {
        log.info("catchup confirm checkout for order {}", order.getOrderId());

        Order orderConfirmed = catchupOrder.confirmCheckout(order)
                .block();

        if (orderConfirmed.getProcessingStatus().equals(WAITING_TIMEOUT.getValue())
                && !isDlt) {
            throw new PaymentMethodException("error occurred during consuming order to confirm payment for order:" + order.getOrderId() + " PaymentIntentId: " + order.getPaymentInfo().getPaymentIntentId());

        } else if (orderConfirmed.getProcessingStatus().equals(WAITING_TIMEOUT.getValue())
                && isDlt) {
            dltConfirmOrders.putIfAbsent(order.getOrderId(), order);
            return;
        }

        dltConfirmOrders.remove(order.getOrderId());

    }


    private boolean acknowledgeAlreadyProcessedOrders(Order order, Acknowledgment acknowledgment) {
        if (nackOrders.get(order.getOrderId()) != null) {
            try {
                acknowledgment.acknowledge();
                log.info("ack order {} and remove it from the nack orders", order.getOrderId());
                nackOrders.remove(order.getOrderId());
                return true;
            } catch (Throwable throwable) {
                nackOrders.put(order.getOrderId(), order);
                log.error("cannot commit offset for order {}, we will add it to nack orders", order.getOrderId(), throwable);
            }
        }
        return false;
    }
}
