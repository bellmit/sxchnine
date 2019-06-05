package com.project.business;

import com.project.model.OrderId;
import com.project.repository.OrderByOrderIdRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderIdService {

    private final OrderByOrderIdRepository orderByOrderIdRepository;

    public OrderIdService(OrderByOrderIdRepository orderByOrderIdRepository) {
        this.orderByOrderIdRepository = orderByOrderIdRepository;
    }

    public OrderId getOrderByOrderId(String uuid){
        return orderByOrderIdRepository.findOrderIdByOrderIdPrimaryKeyOrderId(UUID.fromString(uuid));
    }

    public void saveOrderId(OrderId orderId){
        orderByOrderIdRepository.save(orderId);
    }
}
