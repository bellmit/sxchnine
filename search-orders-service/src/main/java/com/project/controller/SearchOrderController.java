package com.project.controller;

import com.project.business.OrderService;
import com.project.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class SearchOrderController {

    private final OrderService orderService;

    @PostMapping("indexOrder")
    public Mono<Void> indexOrder(@RequestBody Order order) {
        return orderService.indexOrder(order);
    }

    @GetMapping("searchOrders")
    public Flux<Order> searchOrders(@RequestParam(required = false) String user,
                                    @RequestParam(required = false) String orderStatus,
                                    @RequestParam(required = false) String orderTimeFrom,
                                    @RequestParam(required = false) String orderTimeTo,
                                    @RequestParam(required = false) String shippingTimeFrom,
                                    @RequestParam(required = false) String shippingTimeTo){

        return orderService.searchOrders(user, orderStatus, orderTimeFrom, orderTimeTo, shippingTimeFrom, shippingTimeTo);

    }
}
