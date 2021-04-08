package com.project.business;

import com.project.client.PaymentServiceClient;
import com.project.model.*;
import com.project.model.admin.OrdersNumber;
import com.project.producer.OrderProducer;
import com.project.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

import static com.project.utils.OrderHelper.evaluateStatus;
import static org.springframework.cloud.sleuth.instrument.web.WebFluxSleuthOperators.withSpanInScope;
import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentServiceClient paymentServiceClient;
    private final OrderProducer orderProducer;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Flux<Order> getOrderByUserEmail(String email) {
        return orderRepository.findOrdersByUserEmail(email)
                .onErrorResume(error -> {
                    log.error("Cannot retrieve Order for email: {}", email, error);
                    return Mono.empty();
                });
    }

    public Flux<Order> trackOrder(String orderId, String email) {
        if (hasText(orderId) && hasText(email)) {
            return orderRepository.findOrderByOrderIdAndUserEmail(orderId, email)
                    .doOnEach(withSpanInScope(SignalType.ON_COMPLETE, signal -> log.info("Track order with param orderId: {} - email: {}", orderId, email)))
                    .onErrorResume(error -> {
                        log.error("Cannot find Order by order Id: {} - email: {}", orderId, email, error);
                        return Mono.empty();
                    })
                    .flux();

        } else if (hasText(orderId) && !hasText(email)) {
            return getOrderByOrderId(orderId)
                    .doOnEach(withSpanInScope(SignalType.ON_COMPLETE, signal -> log.info("Track order by order ID: {}", orderId)))
                    .onErrorResume(error -> {
                        log.error("Cannot find Order by order Id: {} - email: {}", orderId, email, error);
                        return Mono.empty();
                    })
                    .flux();


        } else if (!hasText(orderId) && hasText(email)) {
            return getOrderByUserEmail(email)
                    .doOnEach(withSpanInScope(SignalType.ON_COMPLETE, signal -> log.info("Track order by email: {}", email)));
        }
        return Flux.empty();
    }

    public Mono<Order> getOrderByOrderId(String orderId){
        return orderRepository.findOrderByOrderId(orderId)
                .doOnError(error -> log.error("Cannot retrieve Order by Order ID: {}", orderId, error));

    }

    public Mono<PaymentResponse> checkoutOrderAndSave(Order order) {
        PaymentResponseWrapper paymentResponseReceived = new PaymentResponseWrapper();

        // set the right total
        setOrderTotal(order);

        return paymentServiceClient.payOrder(order, paymentResponseReceived)
                .doOnEach(withSpanInScope(SignalType.ON_NEXT, signal -> log.info("Checkout order ID: {}", order.getOrderId())))
                .map(paymentResponse -> {
                    order.setPaymentStatus(paymentResponse.getStatus());
                    order.setOrderStatus(evaluateStatus(paymentResponse.getStatus()));
                    order.getPaymentInfo().setPaymentIntentId(paymentResponse.getPaymentIntentId());
                    order.getPaymentInfo().setNoCreditCard(bCryptPasswordEncoder.encode(order.getPaymentInfo().getNoCreditCard()));
                    return order;
                })
                .flatMap(this::saveOrderAndSendToKafka)
                .then(Mono.defer(() -> Mono.just(paymentResponseReceived.getPaymentResponse() != null ? paymentResponseReceived.getPaymentResponse() : new PaymentResponse())));
    }

    public Mono<PaymentResponse> confirmOrderAndSave(String paymentIntentId, String orderId) {
        PaymentResponseWrapper paymentResponseReceived = new PaymentResponseWrapper();
        return paymentServiceClient.confirmPay(paymentIntentId, paymentResponseReceived)
                .doOnEach(withSpanInScope(SignalType.ON_NEXT, signal -> log.info("Confirm order ID: {} with payment intent ID: {}", orderId, paymentIntentId)))
                .flatMap(paymentResponse -> updateOrderStatus(orderId, paymentResponse.getStatus(), paymentIntentId))
                .flatMap(this::saveOrderAndSendToKafka)
                .then(Mono.defer(() -> Mono.just(paymentResponseReceived.getPaymentResponse())));
    }

    public Mono<Void> saveOrderAndSendToKafka(Order order) {
        return saveOrder(order)
                .then(orderProducer.sendOder(Mono.just(order)))
                .then();
    }

    private Mono<Order> saveOrder(Order order){
        return orderRepository.save(order)
                .doOnEach(withSpanInScope(SignalType.ON_NEXT, signal -> log.info("Save Order - ID: {}", order.getOrderId())))
                .doOnError(error -> log.error("Cannot save Order {}", order.getOrderId(), error));
    }

    private void setOrderTotal(Order order) {
        if (order != null && !CollectionUtils.isEmpty(order.getProducts())) {
            BigDecimal total = order.getProducts()
                    .stream()
                    .map(Product::getUnitPrice)
                    .reduce(BigDecimal.valueOf(0), BigDecimal::add);

            order.setTotal(total);
        }
    }

    private Mono<Order> updateOrderStatus(String orderId, String paymentStatus, String paymentIntentId) {
        if (hasText(orderId)) {
            return orderRepository.findOrderByOrderId(orderId)
                    .map(retrievedOrder -> {
                        retrievedOrder.setPaymentStatus(paymentStatus);
                        retrievedOrder.setOrderStatus(evaluateStatus(paymentStatus));
                        if (retrievedOrder.getPaymentInfo() != null) {
                            retrievedOrder.getPaymentInfo().setPaymentIntentId(paymentIntentId);
                        } else {
                            retrievedOrder.setPaymentInfo(fallbackPaymentInfo(paymentIntentId));
                        }
                        return retrievedOrder;
                    });
        } else {
            return Mono.empty();
        }
    }

    private PaymentInfo fallbackPaymentInfo(String paymentIntentId) {
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setPaymentIntentId(paymentIntentId);
        return paymentInfo;
    }

    public Flux<Order> getLastOrdersForCurrentMonth(){
        return orderRepository.findOrdersByOrderTimeGreaterThanEqual(LocalDateTime.now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .withDayOfMonth(1));
    }

}
