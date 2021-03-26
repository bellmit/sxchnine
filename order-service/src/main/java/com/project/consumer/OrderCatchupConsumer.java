package com.project.consumer;

import com.project.business.OrderService;
import com.project.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class OrderCatchupConsumer {

    private static final String DLT = "-dlt";

    private final OrderService orderService;
    private final Map<String, Order> nackOrders = new ConcurrentHashMap<>();
    private final Map<String, Order> nackDLTOrders = new ConcurrentHashMap<>();
    private final Map<String, Order> failedOrders = new ConcurrentHashMap<>();

    public OrderCatchupConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(groupId = "${kafka.consumer.groupId}",
            topics = "${kafka.consumer.topic}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumeCatchupOrder(Order order, Acknowledgment ack) {
        log.info("order to catchup {}", order.getOrderId());
        if (nackOrders.get(order.getOrderId()) != null) {
            ack.acknowledge();
            nackOrders.remove(order.getOrderId());

        } else {

            saveOrder(order, false);

            try {
                ack.acknowledge();
            } catch (Throwable throwable) {
                nackOrders.put(order.getOrderId(), order);
                log.error("cannot commit offset for order {}, we will add it to nack orders", order.getOrderId(), throwable);
            }
        }
    }

    @KafkaListener(groupId = "${kafka.consumer.dlt.groupId}" + DLT,
            topics = "${kafka.consumer.dlt.topic}" + DLT,
            containerFactory = "dltKafkaListenerContainerFactory")
    public void consumeDLTCatchupOrder(Order order, Acknowledgment ack) {
        log.info("DLT - order to catchup {}", order.getOrderId());

        if (nackDLTOrders.get(order.getOrderId()) != null) {
            ack.acknowledge();
            nackDLTOrders.remove(order.getOrderId());
        } else {

            saveOrder(order, true);

            try {
                ack.acknowledge();
            } catch (Throwable throwable) {
                nackDLTOrders.put(order.getOrderId(), order);
                log.error("cannot commit offset for order {}, we will add it to nack orders", order.getOrderId(), throwable);
            }

        }
    }

    @Scheduled(fixedDelay = 600000L)
    public void catchFailedOrders() {
        if (!failedOrders.isEmpty()) {
            failedOrders.forEach((orderId, order) -> saveOrder(order, true));
        }
    }

    private void saveOrder(Order order, boolean isDLT) {
        try {
            orderService.saveOrderAndSendToKafka(order).block();

            failedOrders.remove(order.getOrderId());

        } catch (Throwable throwable) {
            if (isDLT) {
                failedOrders.putIfAbsent(order.getOrderId(), order);
                log.error("Error occurred during saving order {} from DLT. We will add it to failedOrders for processing later", order.getOrderId(), throwable);

            } else {
                throw new RuntimeException("Error occurred during saving to DB order: " + order.getOrderId(), throwable);
            }
        }
    }
}
