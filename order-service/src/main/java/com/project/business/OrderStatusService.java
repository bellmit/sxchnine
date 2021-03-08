package com.project.business;

import com.project.mapper.OrderMapper;
import com.project.model.Order;
import com.project.model.OrderStatus;
import com.project.model.admin.OrdersNumber;
import com.project.repository.OrderStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.cloud.sleuth.instrument.web.WebFluxSleuthOperators.withSpanInScope;
import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderStatusService {

    private final OrderStatusRepository orderStatusRepository;
    private final OrderMapper orderMapper;
    private final Map<String, Integer> map = new ConcurrentHashMap<>();
    private final AtomicLong count = new AtomicLong(0);


    public Mono<OrdersNumber> getOrdersNumber(String date) {
        return getOrdersByOrderStatus(date)
                .groupBy(Order::getOrderStatus)
                .flatMap(stringOrderGroupedFlux -> stringOrderGroupedFlux
                        .collectList()
                        .doOnNext(orders -> map.put(stringOrderGroupedFlux.key(), orders.size())))

                .then(createOrdersNumber());
    }

    public Flux<Order> getOrdersByOrderStatus(String date) {
        if (!hasText(date)) {
            date = LocalDateTime.now().format(ofPattern("yyyyMM"));
        }
        String finalDate = date;
        return orderStatusRepository
                .findOrderStatusesByOrderStatusKeyBucket(date)
                .map(orderMapper::asOrderByOrderStatus)
                .doOnEach(withSpanInScope(SignalType.ON_COMPLETE, signal -> log.info("Get last orders by {}", finalDate)));
    }

    public Flux<Order> getPeriodicOrders(String date, int ordersCount) {
        return Mono.defer(() -> Mono.just(count.getAndSet(ordersCount)))
                .thenMany(Flux.interval(Duration.ofSeconds(10))
                        .flatMap(c -> getLastOrdersByDelta(date, count)));
    }

    private Flux<Order> getLastOrdersByDelta(String date, AtomicLong count) {
        return getOrdersByOrderStatus(date).count()
                .flatMapMany(secondCount -> {
                    log.info("first count {}", count.get());
                    log.info("second count {}", secondCount);
                    if (secondCount != count.get()) {
                        log.info("second count is different {}", secondCount);
                        Flux<Order> orderFlux = getOrdersByOrderStatus(date)
                                .takeLast((int) (secondCount - count.get()));
                        count.getAndSet(secondCount);
                        return orderFlux;
                    }
                    log.info("nothing to show");
                    return Flux.empty();
                });
    }


    private Mono<OrdersNumber> createOrdersNumber() {
        return Mono.defer(() -> Mono.just(new OrdersNumber(map)));
    }

    public Mono<Void> saveOrderStatus(OrderStatus orderStatus) {
        return orderStatusRepository
                .save(orderStatus)
                .doOnEach(withSpanInScope(SignalType.ON_NEXT, signal -> log.info("Save Order Status - ID{}", orderStatus.getOrderStatusKey().getOrderId())))
                .then();
    }
}
