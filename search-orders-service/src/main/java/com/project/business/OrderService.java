package com.project.business;

import com.project.model.Order;
import com.project.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void indexOrder(Order order) {
        orderRepository.save(order);
    }
}
