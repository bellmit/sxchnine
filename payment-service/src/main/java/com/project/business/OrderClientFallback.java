package com.project.business;

import com.project.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderClientFallback {

    private OrderProducer orderProducer;

    public OrderClientFallback(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    public void saveOrder(Order order) {
        log.info("fallback - send order {}", order.getOrderPrimaryKey().getOrderId());
        orderProducer.sendOrder(order);
    }
}
