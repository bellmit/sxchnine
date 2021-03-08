package com.project.business;

import com.project.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static com.project.utils.OrderProcessingStatus.ORDER_PAYMENT_PROCESS;
import static com.project.utils.OrderProcessingStatus.ORDER_PAYMENT_PROCESSED;
import static com.project.utils.PaymentStatusCode.*;

@Service
@Slf4j
public class CatchupOrder {

    private final WebClient orderClient;
    private final OrderProducer orderProducer;
    private final PaymentService paymentService;

    public CatchupOrder(@Qualifier("orderWebClient") WebClient orderClient, OrderProducer orderProducer, PaymentService paymentService) {
        this.orderClient = orderClient;
        this.orderProducer = orderProducer;
        this.paymentService = paymentService;
    }

    public Mono<Order> catchUpCheckout(Order order) {
        order.setProcessingStatus(ORDER_PAYMENT_PROCESS.getValue());
        return paymentService.checkout(order)
                .map(paymentResponse -> {
                    order.setPaymentStatus(paymentResponse.getStatus());
                    order.setOrderStatus(paymentResponse.getStatus());
                    order.getPaymentInfo().setPaymentIntentId(paymentResponse.getPaymentIntentId());
                    order.setProcessingStatus(evaluateProcessingStatus(paymentResponse.getStatus()));
                    order.setTypeProcessing(CHECKOUT_OP.getValue());
                    return order;
                })
                .flatMap(this::saveOrder);
    }

    public Mono<Order> confirmCheckout(Order order) {
        order.setProcessingStatus(ORDER_PAYMENT_PROCESS.getValue());
        return paymentService.checkout3DSecure(order.getPaymentInfo().getPaymentIntentId())
                .map(paymentResponse -> {
                    order.setPaymentStatus(paymentResponse.getStatus());
                    order.setOrderStatus(paymentResponse.getStatus());
                    order.setProcessingStatus(evaluateProcessingStatus(paymentResponse.getStatus()));
                    order.setTypeProcessing(CONFIRM_OP.getValue());

                    return order;
                })
                .flatMap(this::saveOrder);
    }

    public Mono<Order> saveOrder(Order order) {
        return orderClient.post()
                .uri("/save")
                .body(BodyInserters.fromValue(order))
                .exchangeToMono(clientResponse -> {
                    if (!clientResponse.statusCode().is2xxSuccessful()) {
                        log.info("Error occurred while saving order -> we will sent order to kafka");
                        orderProducer.sendOrder(order);
                    }
                    return Mono.defer(() -> Mono.just(order));
                })
                .timeout(Duration.ofSeconds(20))
                .onErrorResume(error -> orderProducer.sendOrder(order));
    }

    private String evaluateProcessingStatus(String status) {
        if (!status.equals(WAITING_TIMEOUT.getValue()))
            return ORDER_PAYMENT_PROCESSED.getValue();
        else
            return WAITING_TIMEOUT.getValue();
    }
}
