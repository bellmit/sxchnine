package com.project.business;

import com.project.mapper.OrderMapper;
import com.project.model.Order;
import com.project.model.OrderStatus;
import com.project.repository.OrderStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static com.project.utils.OrderStatusEnum.*;
import static java.time.format.DateTimeFormatter.ofPattern;

@Service
@RequiredArgsConstructor
public class OrderStatusService {

    private final OrderStatusRepository orderStatusRepository;

    private final OrderMapper orderMapper;

    public Flux<Order> getOrdersByOrderStatus(){
        return orderStatusRepository
                .findOrderStatusesByOrderStatusKeyBucket(LocalDateTime.now().format(ofPattern("yyyyMM")))
                .filter(orderStatus -> orderStatus.getOrderStatus().equals(ORDERED.getValue())
                        || orderStatus.getOrderStatus().equals(PROCESSING.getValue())
                        || orderStatus.getOrderStatus().equals(PREPARE_TO_SHIP.getValue()))
                .map(orderMapper::asOrderByOrderStatus);
    }

    public Mono<Void> saveOrderStatus(OrderStatus orderStatus){
        return orderStatusRepository.save(orderStatus).then();
    }
}
