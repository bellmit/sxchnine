package com.project.business;

import com.project.model.OrderId;
import com.project.repository.OrderByOrderIdRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderIdService {

    private final OrderByOrderIdRepository orderByOrderIdRepository;

    public OrderIdService(OrderByOrderIdRepository orderByOrderIdRepository) {
        this.orderByOrderIdRepository = orderByOrderIdRepository;
    }

    public Mono<OrderId> getOrderByOrderId(String uuid){
        return orderByOrderIdRepository.findOrderIdByOrderIdPrimaryKeyOrderId(uuid);
    }

    public Mono<Void> saveOrderId(OrderId orderId){
        return orderByOrderIdRepository.save(orderId)
                .then();
    }
}
