package com.project.business;

import com.project.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderConsumer {

    private final OrderService orderService;

    public OrderConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(groupId = "${kafka.groupId}", topics = "${kafka.topic}")
    public void consumeOrder(Order order, Acknowledgment acknowledgment) {
        log.info("*******************");
        log.info("consume order {} ", order.toString());
        log.info("*******************");
        order.setOrderId(order.getOrderKey().getOrderId());
        orderService.indexOrder(order);
        acknowledgment.acknowledge();
    }
}
