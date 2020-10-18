package com.project.business;

import com.project.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

    public Mono<Void> catchUpCheckout(Order order) {
        return paymentService.checkout(order)
                .map(paymentResponse -> {
                    order.setPaymentStatus(paymentResponse.getStatus());
                    return order;
                })
                .flatMap(this::saveOrder)
                .then();
    }

    private Mono<Void> saveOrder(Order order){
        return orderClient.post()
                .uri("/save")
                .body(BodyInserters.fromValue(order))
                .exchange()
                .flatMap(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        return Mono.empty();
                    } else {
                        log.info("Error occurred while saving order -> we will sent order to kafka");
                        return orderProducer.sendOrder(order);
                    }
                })
                .onErrorResume(error -> orderProducer.sendOrder(order));
    }
}
