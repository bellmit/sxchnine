package com.project.business;

import com.project.mapper.OrderMapper;
import com.project.model.Order;
import com.project.model.OrderId;
import com.project.repository.OrderByOrderIdRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.sleuth.instrument.web.WebFluxSleuthOperators;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import static org.springframework.cloud.sleuth.instrument.web.WebFluxSleuthOperators.withSpanInScope;

@Service
@RequiredArgsConstructor
@Slf4j
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
                .doOnEach(withSpanInScope(SignalType.ON_NEXT, signal -> log.info("Save OrderId - ID: {}", orderId)))
                .then();
    }

    public Mono<Order> getMappedOrderByOrderId(String orderId){
        return getOrderByOrderId(orderId).map(orderMapper::asOrder)
                .doOnEach(withSpanInScope(SignalType.ON_NEXT, signal -> log.info("Get Mapped Order {}", orderId)));
    }
}
