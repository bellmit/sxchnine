package com.project.client;

import com.project.model.Order;
import com.project.model.PaymentResponse;
import com.project.model.PaymentResponseWrapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static com.project.utils.PaymentStatusCode.*;

@Component
public class PaymentServiceClient {

    private final WebClient webClient;

    public PaymentServiceClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<PaymentResponse> payOrder(Order order, PaymentResponseWrapper paymentResponseWrapper){
        return webClient.post()
                .uri("/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(order))
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .timeout(Duration.ofSeconds(15))
                .onErrorReturn(buildPaymentResponseFallBack(CHECKOUT_OP.getValue()))
                .doOnSuccess(paymentResponseWrapper::setPaymentResponse);
    }

    public Mono<PaymentResponse> confirmPay(String paymentIntentId, PaymentResponseWrapper paymentResponseReceived){
        return webClient.post()
                .uri("/confirmPay/"+paymentIntentId)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .timeout(Duration.ofSeconds(15))
                .onErrorReturn(buildPaymentResponseFallBack(CONFIRM_OP.getValue()))
                .doOnSuccess(paymentResponseReceived::setPaymentResponse);
    }

    private PaymentResponse buildPaymentResponseFallBack(String operation){
        PaymentResponse paymentResponse = new PaymentResponse();
        if (operation.equals(CHECKOUT_OP.getValue())){
            paymentResponse.setStatus(WAITING.getValue());
        } else {
            paymentResponse.setStatus(CANNOT_CONFIRMED.getValue());
        }
        return paymentResponse;
    }
}

