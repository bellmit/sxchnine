package com.project.business;

import com.project.mapper.OrderMapper;
import com.project.model.Order;
import com.project.model.OrderStatus;
import com.project.model.admin.OrdersNumber;
import com.project.repository.OrderStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.project.utils.OrderStatusEnum.*;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
public class OrderStatusService {

    private final OrderStatusRepository orderStatusRepository;
    private final OrderMapper orderMapper;
    private final Map<String, Integer> map = new ConcurrentHashMap<>();


    public Mono<OrdersNumber> getOrdersNumber(String date){
        return getOrdersByOrderStatus(date)
                .groupBy(Order::getOrderStatus)
                .flatMap(stringOrderGroupedFlux -> stringOrderGroupedFlux
                        .collectList()
                        .doOnNext(orders -> map.put(stringOrderGroupedFlux.key(), orders.size())))

                .then(createOrdersNumber());
    }

    public Flux<Order> getOrdersByOrderStatus(String date){
        if (!hasText(date)){
            date = LocalDateTime.now().format(ofPattern("yyyyMM"));
        }
        return orderStatusRepository
                .findOrderStatusesByOrderStatusKeyBucket(date)
                .map(orderMapper::asOrderByOrderStatus);
    }

    private Mono<OrdersNumber> createOrdersNumber(){
        return Mono.defer(() -> Mono.just(new OrdersNumber(map)));
    }

    public Mono<Void> saveOrderStatus(OrderStatus orderStatus){
        return orderStatusRepository.save(orderStatus).then();
    }
}
