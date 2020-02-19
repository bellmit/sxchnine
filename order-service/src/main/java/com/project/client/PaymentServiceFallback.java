package com.project.client;

import com.project.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.project.utils.PaymentStatusCode.WAITING;

@Component
@Slf4j
public class PaymentServiceFallback implements PaymentServiceClient{

    @Override
    public int payOrder(Order order) {
        log.info("fallback - payment service. order: {}", order.toString());
        return WAITING.getCode();
    }
}
