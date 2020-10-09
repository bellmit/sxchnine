package com.project.business;

import com.project.model.*;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedHashMap;

import static com.project.utils.PaymentStatusCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final WebClient webClient;

    public Mono<PaymentResponse> checkout(Order order) {
        return callPaymentMethod(order)
                .flatMap(paymentMethod -> callConfirmPayment(paymentMethod, order));
    }

    private Mono<PaymentResponse> callPaymentMethod(Order order) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.add("type", order.getPaymentInfo().getType());
        params.add("card[number]", order.getPaymentInfo().getNoCreditCard().strip());
        params.add("card[exp_month]", order.getPaymentInfo().getExpDate().split("/")[0]);
        params.add("card[exp_year]", order.getPaymentInfo().getExpDate().split("/")[1]);
        params.add("card[cvc]", String.valueOf(order.getPaymentInfo().getSecurityCode()));

        return webClient.post()
                .uri("/v1/payment_methods")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(params))
                .exchange()
                .flatMap(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        return clientResponse.bodyToMono(Object.class)
                                .map(o -> buildPaymentResponse((LinkedHashMap) o));
                    } else {
                        log.warn("Publish to kafka to be treated after");
                        return clientResponse.bodyToMono(Object.class)
                                .map(o -> buildErrorPaymentResponse((LinkedHashMap) o));
                    }
                })
                .timeout(Duration.ofSeconds(10));
    }


    private Mono<PaymentResponse> callConfirmPayment(PaymentResponse paymentResponse, Order order) {
        if (StringUtils.hasText(paymentResponse.getPaymentMethodId())) {
            PaymentIntentCreateParams paymentIntent = buildPaymentIntent(paymentResponse, order);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("amount", paymentIntent.getAmount().toString());
            params.add("currency", paymentIntent.getCurrency());
            params.add("payment_method", paymentIntent.getPaymentMethod());
            params.add("confirm", paymentIntent.getConfirm().toString());
            //params.add("confirmation_method", paymentIntent.getConfirmationMethod().getValue());
            params.add("confirmation_method", PaymentIntentCreateParams.ConfirmationMethod.MANUAL.getValue());
            params.add("return_url", "http://localhost:3000/orders");

            return webClient.post()
                    .uri("/v1/payment_intents")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(params))
                    .exchange()
                    .flatMap(clientResponse -> {
                        if (clientResponse.statusCode().is2xxSuccessful()) {
                            return clientResponse.bodyToMono(Object.class)
                                    .map(o -> buildPaymentResponse((LinkedHashMap) o));
                        } else {
                            log.warn("Publish to kafka to be treated after");
                            return clientResponse.bodyToMono(Object.class)
                                    .map(o -> buildErrorPaymentResponse((LinkedHashMap) o));
                        }
                    })
                    .log();

        } else if (StringUtils.hasText(order.getPaymentInfo().getPaymentIntentId())) {
            return webClient.post()
                    .uri("/v1/payment_intents/" + order.getPaymentInfo().getPaymentIntentId() + "/confirm")
                    .exchange()
                    .flatMap(clientResponse -> {
                        if (clientResponse.statusCode().is2xxSuccessful()) {
                            return clientResponse.bodyToMono(Object.class)
                                    .map(o -> buildPaymentResponse((LinkedHashMap) o));
                        } else {
                            log.warn("Publish to kafka to be treated after");
                            return clientResponse.bodyToMono(Object.class)
                                    .map(o -> buildErrorPaymentResponse((LinkedHashMap) o));
                        }
                    })
                    .log();
        } else {
            return Mono.just(paymentResponse);
        }
    }

    private PaymentResponse buildErrorPaymentResponse(LinkedHashMap response) {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setStatus(REFUSED.getValue());
        if (response.get("error") != null) {
            Object error = response.get("error");
            ErrorReason errorReason = new ErrorReason(
                    ((LinkedHashMap) error).get("code") != null ? ((LinkedHashMap) error).get("code").toString() : "",
                    ((LinkedHashMap) error).get("decline_code") != null ? ((LinkedHashMap) error).get("decline_code").toString() : "");
            paymentResponse.setErrorReason(errorReason);
        }
        return paymentResponse;
    }

    private PaymentResponse buildPaymentResponse(LinkedHashMap response) {
        PaymentResponse paymentResponse = new PaymentResponse();
        if (response.get("object").equals("payment_method")) {
            paymentResponse.setPaymentMethodId(response.get("id").toString());
        } else {
            paymentResponse.setPaymentIntentId(response.get("id").toString());
            paymentResponse.setPaymentClientSecret(response.get("client_secret").toString());
            if ((response.get("status")).equals("requires_action")) {
                paymentResponse.setActionRequired(true);
                paymentResponse.setNextAction(response.get("next_action") != null
                        && ((LinkedHashMap)response.get("next_action")).get("redirect_to_url") != null
                        && ((LinkedHashMap)((LinkedHashMap)response.get("next_action")).get("redirect_to_url")).get("url") != null
                        ? ((LinkedHashMap)((LinkedHashMap)response.get("next_action")).get("redirect_to_url")).get("url") .toString() : null);
                paymentResponse.setStatus(REQUIRED_ACTION.getValue());
            } else if ((response.get("status")).equals("succeeded")) {
                paymentResponse.setStatus(CONFIRMED.getValue());
            } else {
                paymentResponse.setStatus(REFUSED.getValue());
            }
        }
        return paymentResponse;
    }

    private PaymentIntentCreateParams buildPaymentIntent(PaymentResponse paymentResponse, Order order) {
        return PaymentIntentCreateParams.builder()
                .setAmount(sumTotal(order).longValue() * 100)
                .setCurrency("cad")
                .setPaymentMethod(paymentResponse.getPaymentMethodId())
                .setConfirm(true)
                .setErrorOnRequiresAction(true)
                .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                .build();
    }

    private BigDecimal sumTotal(Order order) {
        if (order != null && !CollectionUtils.isEmpty(order.getProducts())) {
            return order.getProducts()
                    .stream()
                    .filter(p -> p != null && p.getUnitPrice() != null)
                    .map(Product::getUnitPrice)
                    .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        }
        // TODO: change to Zero
        return BigDecimal.TEN;
    }

    public void recheckout(Order order) {
       /* int checkoutStatus = checkout(order);
        if (checkoutStatus == CONFIRMED.getCode()) {
            order.setPaymentStatus(CONFIRMED.getValue());
        } else if (checkoutStatus == REFUSED.getCode()) {
            order.setPaymentStatus(REFUSED.getValue());
        } else if (checkoutStatus == WAITING.getCode()) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            recheckout(order);
        }*/
        //orderClient.saveOrder(order);
    }
}
