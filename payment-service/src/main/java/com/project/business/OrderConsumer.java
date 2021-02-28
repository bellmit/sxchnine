package com.project.business;

import com.project.exception.CatchupOrdersException;
import com.project.exception.PaymentMethodException;
import com.project.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import static com.project.utils.OrderProcessingStatus.ORDER_SAVE_ERROR;
import static com.project.utils.PaymentStatusCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderConsumer {

    private static final String DLT = ".DLT";

    private final CatchupOrder catchupOrder;
    private final Map<String, Order> nackOrders = new ConcurrentHashMap<>();
    private final Map<String, Order> nackDLTOrders = new ConcurrentHashMap<>();
    private final Map<String, Order> dltCheckoutOrders = new ConcurrentHashMap<>();
    private final Map<String, Order> dltConfirmOrders = new ConcurrentHashMap<>();

    @KafkaListener(groupId = "${kafka.consumer.groupId}",
            topics = "${kafka.consumer.topic}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumeOrder(Order order, Acknowledgment acknowledgment) {
        log.info("***********************************************");
        log.info("***********************************************");
        log.info("consume order {} for payment", order.getOrderKey().getOrderId());
        log.info("***********************************************");
        log.info("***********************************************");
        /*
            Rebalance consumer:
            max.poll.interval.ms = 5min
            heartbeat.interval.ms = 3sec
            session.timeout.ms = 10sec
         */
        if (acknowledgeAlreadyProcessedOrders(order, acknowledgment)) return;

        if (order.getOrderStatus().equalsIgnoreCase(WAITING.getValue())
                && order.getPaymentStatus().equalsIgnoreCase(CHECKOUT_OP.getValue())) {

            catchupCheckout(order, false);

        } else if (order.getOrderStatus().equalsIgnoreCase(WAITING.getValue())
                && order.getPaymentStatus().equalsIgnoreCase(CONFIRM_OP.getValue())) {

            confirmCheckout(order, false);

        }

        try {
            acknowledgment.acknowledge();
        } catch (Throwable throwable) {
            nackOrders.put(order.getOrderKey().getOrderId(), order);
            log.error("cannot commit offset for order {}, we will add it to nack orders", order.getOrderKey().getOrderId(), throwable);
        }


    }

    @KafkaListener(groupId = "${kafka.consumer.groupId}" + DLT,
            topics = "${kafka.consumer.topic}" + DLT,
            containerFactory = "kafkaDLTListenerContainerFactory")
    public void consumerOrderDLT(Order order, Acknowledgment acknowledgment) {
        log.info("***********************************************");
        log.info("***********************************************");
        log.info("DLT consume order {} for payment", order.getOrderKey().getOrderId());
        log.info("***********************************************");
        log.info("***********************************************");
        if (nackDLTOrders.get(order.getOrderKey().getOrderId()) != null){
            acknowledgment.acknowledge();
            nackDLTOrders.remove(order.getOrderKey().getOrderId());

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
                nackDLTOrders.put(order.getOrderKey().getOrderId(), order);
                log.error("cannot commit offset for DLT order {}, we will send it to unack topic", order.getOrderKey().getOrderId(), throwable);
            }
        }
    }

    @Scheduled(cron = "0 0/3 * * * ?")
    public void reprocessFailedDLTOrders(){
        log.info("scheduler start processing failed orders ..");
        if (!dltCheckoutOrders.isEmpty()){
            log.info("checkout failed orders - nbr of elements to process {}", dltCheckoutOrders.size());
            dltCheckoutOrders.forEach((k,v) -> log.info(v.toString()));

            dltCheckoutOrders.forEach((orderId,order) -> catchupCheckout(order, true));

            log.info("finish checkout failed orders - nbr of elements left {}", dltCheckoutOrders.size());
        }
        if (!dltConfirmOrders.isEmpty()){
            log.info("confirm failed orders - nbr of elements to process {}", dltConfirmOrders.size());

            dltConfirmOrders.forEach((orderId,order) -> confirmCheckout(order, true));

            log.info("finish confirm failed orders - nbr of elements left {}", dltConfirmOrders.size());
        }
    }

    private void catchupCheckout(Order order, boolean isDLT) {
        log.info("catchup checkout for order {}", order.getOrderKey().getOrderId());
        Order processedOrder = catchupOrder.catchUpCheckout(order)
                .block();

        if (processedOrder.getProcessingStatus().equals(WAITING_TIMEOUT.getValue())
                && !isDLT) {
            throw new PaymentMethodException("error occurred during consuming order to checkout payment for order:" + order.getOrderKey().getOrderId() + " PaymentIntentId: " + order.getPaymentInfo().getPaymentIntentId());
        } else if (processedOrder.getProcessingStatus().equals(WAITING_TIMEOUT.getValue())
                && isDLT){
            dltCheckoutOrders.putIfAbsent(order.getOrderKey().getOrderId(), order);
            return;
        }

        dltCheckoutOrders.remove(order.getOrderKey().getOrderId());
    }

    private void confirmCheckout(Order order, boolean isDlt) {
        log.info("catchup confirm checkout for order {}", order.getOrderKey().getOrderId());

        Order orderConfirmed = catchupOrder.confirmCheckout(order)
                .block();

        if (orderConfirmed.getProcessingStatus().equals(WAITING_TIMEOUT.getValue())
                && !isDlt) {
            throw new PaymentMethodException("error occurred during consuming order to confirm payment for order:" + order.getOrderKey().getOrderId() + " PaymentIntentId: " + order.getPaymentInfo().getPaymentIntentId());

        } else if (orderConfirmed.getProcessingStatus().equals(WAITING_TIMEOUT.getValue())
                && isDlt) {
            dltConfirmOrders.putIfAbsent(order.getOrderKey().getOrderId(), order);
            return;
        }

        dltConfirmOrders.remove(order.getOrderKey().getOrderId());

    }


    private boolean acknowledgeAlreadyProcessedOrders(Order order, Acknowledgment acknowledgment) {
        if (nackOrders.get(order.getOrderKey().getOrderId()) != null) {
            try {
                acknowledgment.acknowledge();
                log.info("ack order {} and remove it from the nack orders", order.getOrderKey().getOrderId());
                nackOrders.remove(order.getOrderKey().getOrderId());
                return true;
            } catch (Throwable throwable) {
                nackOrders.put(order.getOrderKey().getOrderId(), order);
                log.error("cannot commit offset for order {}, we will add it to nack orders", order.getOrderKey().getOrderId(), throwable);
            }
        }
        return false;
    }
}
