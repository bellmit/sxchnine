package com.project.controller;

import com.project.business.OrderIdService;
import com.project.business.OrderService;
import com.project.business.OrderStatusService;
import com.project.model.Order;
import com.project.model.PaymentResponse;
import com.project.model.admin.OrdersNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    private final OrderIdService orderIdService;

    private final OrderStatusService orderStatusService;


    @GetMapping(value = "/ordersNotification/{ordersSize}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Order> getOrdersNotification(@PathVariable int ordersSize,
                                             @RequestParam(required = false) String date) {
        return orderStatusService.getPeriodicOrders(date, ordersSize);
    }

    @GetMapping("/lastOrders")
    public Flux<Order> getLastOrders(@RequestParam(required = false) String date) {
        return orderStatusService.getOrdersByOrderStatus(date);
    }

    @GetMapping("/orderId/{orderId}")
    public Mono<Order> getOrdersByOrderId(@PathVariable String orderId) {
        return orderIdService.getMappedOrderByOrderId(orderId);
    }

    @GetMapping("/userEmail/{userEmail:.+}")
    public Flux<Order> getOrdersByEmail(@PathVariable String userEmail) {
        return orderService.getOrderByUserEmail(userEmail);
    }

    @GetMapping("/trackOrder")
    public Flux<Order> trackOrder(@RequestParam(required = false) String orderId, @RequestParam(required = false) String email) {
        return orderService.trackOrder(orderId, email);
    }

    @PostMapping("/checkoutOrder")
    public Mono<PaymentResponse> checkoutOrderAndSave(@RequestBody Order order) {
        return orderService.checkoutOrderAndSave(order);
    }

    @PostMapping("/save")
    public Mono<Void> saveOrder(@RequestBody Order order) {
        return orderService.saveOrder(order);
    }

    @PostMapping("/confirmOrder")
    public Mono<PaymentResponse> confirmOrder(@RequestParam String paymentIntentId, @RequestParam(required = false) String orderId) {
        return orderService.confirmOrderAndSave(paymentIntentId, orderId);
    }

    @GetMapping("/admin/ordersNumber")
    public Mono<OrdersNumber> getOrdersNumber(@RequestParam(required = false) String date) {
        return orderStatusService.getOrdersNumber(date);
    }
}
