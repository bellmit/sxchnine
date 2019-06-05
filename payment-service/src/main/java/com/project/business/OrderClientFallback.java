package com.project.business;

import com.project.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderClientFallback implements OrderClient {

    private OrderProducer orderProducer;

    public OrderClientFallback(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @Override
    public void saveOrder(Order order) {
        orderProducer.sendOrder(order);
    }
}
