package com.project.business;

import com.project.mapper.OrderMapper;
import com.project.model.Order;
import com.project.model.OrderId;
import com.project.repository.OrderByOrderIdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderIdService {

    private final OrderByOrderIdRepository orderByOrderIdRepository;
    private final OrderMapper orderMapper;


    public Mono<OrderId> getOrderByOrderId(String orderId){
        return orderByOrderIdRepository
                .findOrderIdByOrderIdKeyOrderId(orderId);
    }

    public Flux<OrderId> getOrderByOrderIdAndEmail(String orderId, String email){
        return orderByOrderIdRepository.findOrderIdByOrderIdKeyOrderIdAndOrderIdKeyUserEmail(orderId, email);
    }

    public Mono<Void> saveOrderId(OrderId orderId){
        return orderByOrderIdRepository.save(orderId)
                .then();
    }

    public Mono<Order> getMappedOrderByOrderId(String orderId){
        return getOrderByOrderId(orderId).map(orderMapper::asOrder);
    }
}
