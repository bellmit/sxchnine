package com.project.business;

import com.project.model.Order;
import com.project.model.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.springframework.cloud.sleuth.instrument.web.WebFluxSleuthOperators.withSpanInScope;
import static reactor.core.publisher.SignalType.ON_COMPLETE;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentOps paymentOps;

    public Mono<PaymentResponse> checkout(Order order) {
        return paymentOps.checkout(order)
                .doOnEach(withSpanInScope(ON_COMPLETE, signal -> log.info("Checkout payment for order id {}", order.getOrderKey().getOrderId())));
    }

    public Mono<PaymentResponse> checkout3DSecure(String paymentIntentId) {
        return paymentOps.checkout3DSecure(paymentIntentId)
                .doOnEach(withSpanInScope(ON_COMPLETE, signal -> log.info("Checkout 3D Secure - Confirm payment id {}", paymentIntentId)));
    }
}
