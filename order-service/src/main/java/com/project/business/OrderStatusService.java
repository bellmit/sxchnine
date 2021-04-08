package com.project.business;

import com.project.model.Order;
import com.project.model.admin.OrdersNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.cloud.sleuth.instrument.web.WebFluxSleuthOperators.withSpanInScope;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderStatusService {

    private final OrderService orderService;
    private final AtomicInteger ordersSize = new AtomicInteger(0);

    public Mono<OrdersNumber> getOrdersNumber(String date) {
        final Map<String, Integer> ordersNumber = new ConcurrentHashMap<>();
        return getLastOrdersForCurrentMonth(date)
                .groupBy(Order::getOrderStatus)
                .flatMap(stringOrderGroupedFlux -> stringOrderGroupedFlux
                        .collectList()
                        .doOnNext(orders -> ordersNumber.put(stringOrderGroupedFlux.key(), orders.size())))

                .then(createOrdersNumber(ordersNumber));
    }

    public Flux<Order> getLastOrdersForCurrentMonth(String date) {
        return orderService
                .getLastOrdersForCurrentMonth()
                .doOnEach(withSpanInScope(SignalType.ON_COMPLETE, signal -> log.info("Get last orders by {}/{}", LocalDate.now().getMonthValue(), LocalDate.now().getYear())));
    }

    public Flux<Order> getPeriodicOrders(String date, int ordersCount) {
        return Mono.defer(() -> Mono.just(ordersSize.getAndSet(ordersCount)))
                .thenMany(Flux.interval(Duration.ofSeconds(10))
                        .flatMap(c -> getLastOrdersByDelta(date, ordersSize)));
    }

    private Flux<Order> getLastOrdersByDelta(String date, AtomicInteger ordersSize) {
        return getLastOrdersForCurrentMonth(date).count()
                .flatMapMany(secondCount -> {
                    if (secondCount != ordersSize.get()) {
                        Flux<Order> orderFlux = getLastOrdersForCurrentMonth(date)
                                .takeLast((int) (secondCount - ordersSize.get()));
                        ordersSize.getAndSet(secondCount.intValue());
                        return orderFlux;
                    }
                    return Flux.empty();
                });
    }


    private Mono<OrdersNumber> createOrdersNumber(Map<String, Integer> ordersNumber) {
        return Mono.defer(() -> Mono.just(new OrdersNumber(ordersNumber)));
    }
}
