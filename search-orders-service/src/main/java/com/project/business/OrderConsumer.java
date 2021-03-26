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
        log.info("consume order {} ", order.getOrderId());
        orderService.indexOrder(order)
                .subscribe(
                        o -> log.info("order {} is indexed into ES successfully", order.getOrderId()),
                        error -> log.error("order {} cannot get indexed into ES", order.getOrderId(), error));
        acknowledgment.acknowledge();
    }
}
