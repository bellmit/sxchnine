package com.project.consumer;

import com.project.business.OrdersCreator;
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

    private static final String DLT = ".DLT";

    private final OrdersCreator orderService;
    private final Map<String, Order> nackOrders = new ConcurrentHashMap<>();
    private final Map<String, Order> nackDLTOrders = new ConcurrentHashMap<>();
    private final Map<String, Order> failedOrders = new ConcurrentHashMap<>();

    public OrderCatchupConsumer(OrdersCreator orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(groupId = "${kafka.consumer.groupId}", topics = "${kafka.consumer.topic}")
    public void consumeCatchupOrder(Order order, Acknowledgment ack) {
        log.info("***************************************");
        log.info("order to catchup {}", order.toString());
        log.info("***************************************");

        if (nackOrders.get(order.getOrderKey().getOrderId()) != null) {
            ack.acknowledge();
            nackOrders.remove(order.getOrderKey().getOrderId());

        } else {

            saveOrder(order, false);

            try {
                ack.acknowledge();
            } catch (Throwable throwable) {
                nackOrders.put(order.getOrderKey().getOrderId(), order);
                log.error("cannot commit offset for order {}, we will add it to nack orders", order.getOrderKey().getOrderId(), throwable);
            }
        }
    }

    @KafkaListener(groupId = "${kafka.consumer.groupId}" + DLT, topics = "${kafka.consumer.topic}" + DLT)
    public void consumeDLTCatchupOrder(Order order, Acknowledgment ack) {
        log.info("***************************************");
        log.info("DLT - order to catchup {}", order.toString());
        log.info("***************************************");

        if (nackDLTOrders.get(order.getOrderKey().getOrderId()) != null) {
            ack.acknowledge();
            nackDLTOrders.remove(order.getOrderKey().getOrderId());
        } else {

            saveOrder(order, true);

            try {
                ack.acknowledge();
            } catch (Throwable throwable) {
                nackDLTOrders.put(order.getOrderKey().getOrderId(), order);
                log.error("cannot commit offset for order {}, we will add it to nack orders", order.getOrderKey().getOrderId(), throwable);
            }

        }
    }

    @Scheduled(cron = "* 0/10 * * * ?")
    public void catchFailedOrders() {
        if (!failedOrders.isEmpty()) {
            failedOrders.forEach((orderId, order) -> saveOrder(order, true));
        }
    }

    private void saveOrder(Order order, boolean isDLT) {
        try {
            orderService.saveOrders(order).block();

            failedOrders.remove(order.getOrderKey().getOrderId());

        } catch (Throwable throwable) {
            if (isDLT) {
                failedOrders.putIfAbsent(order.getOrderKey().getOrderId(), order);
                log.error("Error occurred during saving order {} from DLT. We will add it to failedOrders for processing later", order.getOrderKey().getOrderId(), throwable);

            } else {
                throw new RuntimeException("Error occurred during saving to DB order: " + order.getOrderKey().getOrderId(), throwable);
            }
        }
    }
}
