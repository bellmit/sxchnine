package com.project.client;

import com.project.model.Order;
import com.project.model.PaymentResponse;
import org.springframework.http.MediaType;
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

    public Mono<PaymentResponse> payOrder(Order order){
        return webClient.post()
                .uri("/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(order))
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .timeout(Duration.ofSeconds(15))
                .onErrorReturn(buildPaymentResponseFallBack());
    }

    private PaymentResponse buildPaymentResponseFallBack(){
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setStatus(WAITING.getValue());
        return paymentResponse;
    }
}

