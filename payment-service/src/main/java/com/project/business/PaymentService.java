package com.project.business;

import com.project.model.Order;
import com.project.model.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentOps paymentOps;

    public Mono<PaymentResponse> checkout(Order order) {
        return paymentOps.checkout(order);
    }

    public Mono<PaymentResponse> checkout3DSecure(String paymentIntentId) {
        return paymentOps.checkout3DSecure(paymentIntentId);
    }
}
