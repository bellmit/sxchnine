package com.project.business;

import com.project.mapper.OrderMapper;
import com.project.model.Order;
import com.project.model.OrderStatus;
import com.project.repository.OrderStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static com.project.utils.OrderStatusEnum.*;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
public class OrderStatusService {

    private final OrderStatusRepository orderStatusRepository;
    private final OrderMapper orderMapper;

    public Flux<Order> getOrdersByOrderStatus(String date){
        if (!hasText(date)){
            date = LocalDateTime.now().format(ofPattern("yyyyMM"));
        }
        return orderStatusRepository
                .findOrderStatusesByOrderStatusKeyBucket(date)
                .map(orderMapper::asOrderByOrderStatus);
    }

    public Mono<Void> saveOrderStatus(OrderStatus orderStatus){
        return orderStatusRepository.save(orderStatus).then();
    }
}
