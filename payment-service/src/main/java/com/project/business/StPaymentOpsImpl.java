package com.project.business;

import com.project.configuration.OrderConfigurationProperties;
import com.project.model.ErrorReason;
import com.project.model.Order;
import com.project.model.PaymentResponse;
import com.project.utils.PaymentUtil;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;

import static com.project.utils.PaymentStatusCode.*;

@Service
@Slf4j
public class StPaymentOpsImpl implements PaymentOps {

    private final WebClient webClient;
    private final OrderConfigurationProperties orderConfigurationProperties;

    public StPaymentOpsImpl(@Qualifier("vendorWebClient") WebClient webClient, OrderConfigurationProperties orderConfigurationProperties) {
        this.webClient = webClient;
        this.orderConfigurationProperties = orderConfigurationProperties;
    }

    @Override
    public Mono<PaymentResponse> checkout(Order order) {
        return callPaymentMethod(order)
                .flatMap(paymentMethod -> callCreatePaymentIntent(paymentMethod, order));
    }

    @Override
    public Mono<PaymentResponse> checkout3DSecure(String paymentIntentId) {
        return retrievePayment(paymentIntentId)
                .flatMap(this::confirmPayment);

    }

    private Mono<PaymentResponse> callPaymentMethod(Order order) {
        return webClient.post()
                .uri("/v1/payment_methods")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("type", order.getPaymentInfo().getType())
                        .with("card[number]", order.getPaymentInfo().getNoCreditCard().replaceAll("\\s", ""))
                        .with("card[exp_month]", order.getPaymentInfo().getExpDate().split("/")[0])
                        .with("card[exp_year]", order.getPaymentInfo().getExpDate().split("/")[1])
                        .with("card[cvc]", order.getPaymentInfo().getSecurityCode())
                        .with("billing_details[name]", order.getPaymentInfo().getFullName()))
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        log.info("payment method success");
                        return clientResponse.bodyToMono(Object.class)
                                .map(o -> buildPaymentResponse((LinkedHashMap) o));
                    } else {
                        log.error("error payment method");
                        return clientResponse.bodyToMono(Object.class)
                                .map(o -> buildErrorPaymentResponse((LinkedHashMap) o));
                    }
                })
                .onErrorReturn(buildErrorPaymentResponse(CALL_PAYMENT_METHOD_TIMEOUT.getValue()));
    }


    private Mono<PaymentResponse> callCreatePaymentIntent(PaymentResponse paymentResponse, Order order) {
        if (StringUtils.hasText(paymentResponse.getPaymentMethodId())) {
            PaymentIntentCreateParams paymentIntent = buildPaymentIntent(paymentResponse, order);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("amount", paymentIntent.getAmount().toString());
            params.add("currency", paymentIntent.getCurrency());
            params.add("payment_method", paymentIntent.getPaymentMethod());
            params.add("confirm", paymentIntent.getConfirm().toString());
            //params.add("confirmation_method", paymentIntent.getConfirmationMethod().getValue());
            params.add("confirmation_method", PaymentIntentCreateParams.ConfirmationMethod.MANUAL.getValue());
            params.add("return_url", orderConfigurationProperties.getRedirect());
            params.add("metadata[orderId]", order.getOrderId());

            return webClient.post()
                    .uri("/v1/payment_intents")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(params))
                    .exchangeToMono(clientResponse -> {
                        if (clientResponse.statusCode().is2xxSuccessful()) {
                            log.info("create payment intent success");
                            return clientResponse.bodyToMono(Object.class)
                                    .map(o -> buildPaymentResponse((LinkedHashMap) o));
                        } else {
                            log.error("error create payment intent");
                            return clientResponse.bodyToMono(Object.class)
                                    .map(o -> buildErrorPaymentResponse((LinkedHashMap) o));
                        }
                    })
                    .onErrorReturn(buildErrorPaymentResponse(CREATE_PAYMENT_INTENT_TIMEOUT.getValue()));
        } else {
            return Mono.just(paymentResponse);
        }
    }

    private Mono<PaymentResponse> retrievePayment(String paymentIntentId) {
        return webClient.get()
                .uri("/v1/payment_intents/" + paymentIntentId)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        log.info("retrieve payment success");
                        return clientResponse.bodyToMono(Object.class)
                                .map(o -> buildPaymentResponse((LinkedHashMap) o));
                    } else {
                        log.info("error retrieve payment");
                        return clientResponse.bodyToMono(Object.class)
                                .map(o -> buildErrorPaymentResponse((LinkedHashMap) o));
                    }
                })
                .onErrorReturn(buildErrorPaymentResponse(RETRIEVE_PAYMENT_INTENT_TIMEOUT.getValue()));
    }

    private Mono<PaymentResponse> confirmPayment(PaymentResponse paymentResponse) {
        log.info("confirm payment - paymentIntentId = {}", paymentResponse.getPaymentIntentId());
        if (paymentResponse.getStatus().equals(REQUIRED_CONFIRMATION.getValue())
                || paymentResponse.getStatus().equals(CONFIRMED.getValue())) {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("payment_method", "pm_card_visa");
            return webClient.post()
                    .uri("/v1/payment_intents/" + paymentResponse.getPaymentIntentId() + "/confirm")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(params))
                    .exchangeToMono(clientResponse -> {
                        if (clientResponse.statusCode().is2xxSuccessful()) {
                            log.info("confirm payment success");
                            return clientResponse.bodyToMono(Object.class)
                                    .map(o -> buildPaymentResponse((LinkedHashMap) o));
                        } else {
                            log.info("error confirm payment");
                            return clientResponse.bodyToMono(Object.class)
                                    .map(o -> buildErrorPaymentResponse((LinkedHashMap) o));
                        }
                    })
                    .onErrorReturn(buildErrorPaymentResponse(CONFIRM_PAYMENT_TIMEOUT.getValue()));
        } else {
            return Mono.just(paymentResponse);
        }
    }

    private PaymentResponse buildErrorPaymentResponse(String errorStatus) {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setStatus(errorStatus);
        return paymentResponse;
    }

    private PaymentResponse buildErrorPaymentResponse(LinkedHashMap response) {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setStatus(REFUSED.getValue());
        if (response.get("error") != null) {
            Object error = response.get("error");
            ErrorReason errorReason = new ErrorReason(
                    ((LinkedHashMap) error).get("code") != null ? ((LinkedHashMap) error).get("code").toString() : "",
                    ((LinkedHashMap) error).get("decline_code") != null ? ((LinkedHashMap) error).get("decline_code").toString() : "",
                    ((LinkedHashMap) error).get("message") != null ? ((LinkedHashMap) error).get("message").toString() : "");
            paymentResponse.setErrorReason(errorReason);

            log.error("error infos: error code = {} - declined code = {} - reason = {}",
                    errorReason.getCode(),
                    errorReason.getDeclinedCode(),
                    errorReason.getMessage());
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
            if (response.get("metadata") != null
                    && ((LinkedHashMap) response.get("metadata")).get("orderId") != null) {
                paymentResponse.setOrderId(((LinkedHashMap) response.get("metadata")).get("orderId").toString());
            }
            if ((response.get("status")).equals("requires_action")) {
                paymentResponse.setActionRequired(true);
                paymentResponse.setNextAction(response.get("next_action") != null
                        && ((LinkedHashMap) response.get("next_action")).get("redirect_to_url") != null
                        && ((LinkedHashMap) ((LinkedHashMap) response.get("next_action")).get("redirect_to_url")).get("url") != null
                        ? ((LinkedHashMap) ((LinkedHashMap) response.get("next_action")).get("redirect_to_url")).get("url").toString() : null);
                paymentResponse.setStatus(REQUIRED_ACTION.getValue());
            } else if ((response.get("status")).equals("succeeded")) {
                paymentResponse.setStatus(CONFIRMED.getValue());
            } else if (response.get("status").equals("requires_payment_method")) {
                paymentResponse.setStatus(REFUSED.getValue());
            } else if (response.get("status").equals("requires_confirmation")) {
                paymentResponse.setStatus(REQUIRED_CONFIRMATION.getValue());
            } else {
                paymentResponse.setStatus(REFUSED.getValue());
            }
        }
        return paymentResponse;
    }

    private PaymentIntentCreateParams buildPaymentIntent(PaymentResponse paymentResponse, Order order) {
        return PaymentIntentCreateParams.builder()
                .setAmount(PaymentUtil.sumTotal(order).longValue() * 100)
                .setCurrency("cad")
                .setPaymentMethod(paymentResponse.getPaymentMethodId())
                .setConfirm(true)
                .setErrorOnRequiresAction(true)
                .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                .build();
    }
}
