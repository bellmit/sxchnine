package com.project.business;

import com.project.model.IndexedOrder;
import com.project.model.Order;
import com.project.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void indexOrder(List<IndexedOrder> orders){
        orderRepository.saveAll(orders);
    }
}
