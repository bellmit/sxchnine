package com.project.controller;

import com.project.business.PaymentService;
import com.project.model.Order;
import com.project.model.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/pay")
    public Mono<PaymentResponse> payOrder(@RequestBody Order order){
        return paymentService.checkout(order);
    }
}
