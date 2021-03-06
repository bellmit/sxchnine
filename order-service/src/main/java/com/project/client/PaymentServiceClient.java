package com.project.client;

import com.project.config.FeignClientInterceptor;
import com.project.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", fallback = PaymentServiceFallback.class, configuration = FeignClientInterceptor.class)
public interface PaymentServiceClient {

    @PostMapping("/pay")
    int payOrder(@RequestBody Order order);
}

