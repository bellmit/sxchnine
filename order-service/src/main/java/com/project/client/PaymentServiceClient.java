package com.project.client;

import com.project.model.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static com.project.utils.PaymentStatusCode.WAITING;

@Component
public class PaymentServiceClient {

    private final WebClient webClient;

    public PaymentServiceClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Integer> payOrder(Order order){
        return webClient.post()
                .uri("/pay")
                .body(BodyInserters.fromValue(order))
                .retrieve()
                .bodyToMono(Integer.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorReturn(WAITING.getCode());
    }
}

