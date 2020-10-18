package com.project.controller;

import com.project.business.OrderIdService;
import com.project.business.OrderService;
import com.project.model.Order;
import com.project.model.OrderId;
import com.project.model.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE;

@RestController
@Slf4j
public class OrderController {

    private final OrderService orderService;

    private final OrderIdService orderIdService;

    public OrderController(OrderService orderService, OrderIdService orderIdService) {
        this.orderService = orderService;
        this.orderIdService = orderIdService;
    }

    @GetMapping(value = "/all", produces = APPLICATION_STREAM_JSON_VALUE)
    public Flux<Order> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/orderId/{orderId}")
    public Mono<OrderId> getOrdersByOrderId(@PathVariable String orderId){
        return orderIdService.getOrderByOrderId(orderId);
    }

    @GetMapping(value = "/userEmail/{userEmail:.+}")
    public Flux<Order> getOrdersByEmail(@PathVariable String userEmail){
        return orderService.getOrderByUserEmail(userEmail);
    }

    @PostMapping("/checkoutOrder")
    public Mono<PaymentResponse> checkoutOrderAndSave(@RequestBody Order order){
        return orderService.checkoutOrderAndSave(order);
    }

    @PostMapping("/save")
    public Mono<Void> saveOrder(@RequestBody Order order){
        //TODO: delete simulationOrder
        return orderService.saveOrder(order);
    }

    @PostMapping("/confirmOrder")
    public Mono<PaymentResponse> confirmOrder(@RequestParam String paymentIntentId, @RequestParam String orderId){
        return orderService.confirmOrderAndSave(paymentIntentId, orderId);
    }
}
