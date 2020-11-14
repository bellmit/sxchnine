package com.project.controller;

import com.project.business.OrderIdService;
import com.project.business.OrderService;
import com.project.business.OrderStatusService;
import com.project.model.Order;
import com.project.model.OrderId;
import com.project.model.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final OrderIdService orderIdService;

    private final OrderStatusService orderStatusService;


    @GetMapping("/lastOrders")
    public Flux<Order> getLastOrders(@RequestParam(required = false) String date){
        return orderStatusService.getOrdersByOrderStatus(date);
    }

    @GetMapping("/orderId/{orderId}")
    public Mono<OrderId> getOrdersByOrderId(@PathVariable String orderId){
        return orderIdService.getOrderByOrderId(orderId);
    }

    @GetMapping("/userEmail/{userEmail:.+}")
    public Flux<Order> getOrdersByEmail(@PathVariable String userEmail){
        return orderService.getOrderByUserEmail(userEmail);
    }

    @GetMapping("/trackOrder")
    public Flux<Order> trackOrder(@RequestParam String orderId, @RequestParam String email){
        return orderService.trackOrder(orderId, email);
    }

    @PostMapping("/checkoutOrder")
    public Mono<PaymentResponse> checkoutOrderAndSave(@RequestBody Order order){
        return orderService.checkoutOrderAndSave(order);
    }

    @PostMapping("/save")
    public Mono<Void> saveOrder(@RequestBody Order order){
        return orderService.saveOrder(order);
    }

    @PostMapping("/confirmOrder")
    public Mono<PaymentResponse> confirmOrder(@RequestParam String paymentIntentId, @RequestParam String orderId){
        return orderService.confirmOrderAndSave(paymentIntentId, orderId);
    }
}
