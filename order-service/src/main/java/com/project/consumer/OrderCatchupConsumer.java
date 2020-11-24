package com.project.consumer;

import com.project.business.OrdersCreator;
import com.project.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderCatchupConsumer {

    private final OrdersCreator orderService;

    public OrderCatchupConsumer(OrdersCreator orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(groupId = "${kafka.consumer.groupId}", topics = "${kafka.consumer.topic}")
    public void consumeCatchupOrder(Order order, Acknowledgment ack){
        log.info("***************************************");
        log.info("order to catchup {}", order.toString());
        log.info("***************************************");

        orderService.saveOrders(order)
                .subscribe(o -> log.info("Catching up order has been successfully saved"),
                        error -> log.error("error occurred during consuming order to catchup {}", order, error));
        ack.acknowledge();
    }
}
