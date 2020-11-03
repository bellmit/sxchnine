package com.project.business;

import com.project.model.OrderId;
import com.project.repository.OrderByOrderIdRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderIdService {

    private final OrderByOrderIdRepository orderByOrderIdRepository;

    public OrderIdService(OrderByOrderIdRepository orderByOrderIdRepository) {
        this.orderByOrderIdRepository = orderByOrderIdRepository;
    }

    public Mono<OrderId> getOrderByOrderId(String orderId){
        return orderByOrderIdRepository.findOrderIdByOrderIdKeyOrderId(orderId);
    }

    public Flux<OrderId> getOrderByOrderIdAndEmail(String orderId, String email){
        return orderByOrderIdRepository.findOrderIdByOrderIdKeyOrderIdAndOrderIdKeyUserEmail(orderId, email);
    }

    public Mono<Void> saveOrderId(OrderId orderId){
        return orderByOrderIdRepository.save(orderId)
                .then();
    }
}
