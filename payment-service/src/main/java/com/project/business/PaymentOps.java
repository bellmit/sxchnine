package com.project.business;

import com.project.model.Order;
import com.project.model.PaymentResponse;
import reactor.core.publisher.Mono;

public interface PaymentOps {

    Mono<PaymentResponse> checkout(Order order);

    Mono<PaymentResponse> checkout3DSecure(String paymentIntentId);
}
