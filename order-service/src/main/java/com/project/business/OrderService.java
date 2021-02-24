package com.project.business;

import com.project.client.PaymentServiceClient;
import com.project.model.Order;
import com.project.model.PaymentResponse;
import com.project.model.PaymentResponseWrapper;
import com.project.producer.OrderProducer;
import com.project.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import static com.project.utils.OrderHelper.evaluateStatus;
import static org.springframework.cloud.sleuth.instrument.web.WebFluxSleuthOperators.withSpanInScope;
import static org.springframework.util.StringUtils.hasText;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrdersCreator ordersCreator;

    private final PaymentServiceClient paymentServiceClient;

    private final OrderProducer orderProducer;

    public OrderService(OrderRepository orderRepository, OrdersCreator ordersCreator, PaymentServiceClient paymentServiceClient, OrderProducer orderProducer) {
        this.orderRepository = orderRepository;
        this.ordersCreator = ordersCreator;
        this.paymentServiceClient = paymentServiceClient;
        this.orderProducer = orderProducer;
    }

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Flux<Order> getOrderByUserEmail(String userEmail) {
        return orderRepository.findOrdersByOrderKeyUserEmail(userEmail)
                .doOnEach(withSpanInScope(SignalType.ON_COMPLETE, signal -> log.info("Get orders for this user: {}", userEmail) ));
    }

    public Flux<Order> trackOrder(String orderId, String email) {
        if (hasText(orderId) && hasText(email)) {
            return ordersCreator.getOrderByOrderIdAndEmail(orderId, email)
                    .doOnEach(withSpanInScope(SignalType.ON_COMPLETE, signal -> log.info("Track order with param orderId: {} - email: {}", orderId, email)));

        } else if (hasText(orderId) && !hasText(email)) {
            return ordersCreator.getOrderByOrderId(orderId)
                    .doOnEach(withSpanInScope(SignalType.ON_COMPLETE, signal -> log.info("Track order with param orderId: {} - email: {}", orderId, email)));

        } else if (!hasText(orderId) && hasText(email)) {
            return getOrderByUserEmail(email);
        }
        return Flux.empty();
    }

    public Mono<PaymentResponse> checkoutOrderAndSave(Order order) {
        PaymentResponseWrapper paymentResponseReceived = new PaymentResponseWrapper();
        return paymentServiceClient.payOrder(order, paymentResponseReceived)
                .map(paymentResponse -> {
                    order.setPaymentStatus(paymentResponse.getStatus());
                    order.setOrderStatus(evaluateStatus(paymentResponse.getStatus()));
                    order.getPaymentInfo().setPaymentIntentId(paymentResponse.getPaymentIntentId());
                    return order;
                })
                .doOnEach(withSpanInScope(SignalType.ON_NEXT, signal -> log.info("Checkout order ID: {}", order.getOrderKey().getOrderId())))
                .flatMap(ordersCreator::saveOrders)
                .onErrorResume(error -> {
                    log.error("cannot save order to the database, we will send it to kafka as well {}", order.toString(), error);
                    return Mono.empty();
                })
                .then(orderProducer.sendOder(Mono.just(order)))
                .then(Mono.defer(() -> Mono.just(paymentResponseReceived.getPaymentResponse() != null ? paymentResponseReceived.getPaymentResponse() : new PaymentResponse())));
    }

    public Mono<PaymentResponse> confirmOrderAndSave(String paymentIntentId, String orderId) {
        PaymentResponseWrapper paymentResponseReceived = new PaymentResponseWrapper();
        return paymentServiceClient.confirmPay(paymentIntentId, paymentResponseReceived)
                .doOnEach(withSpanInScope(SignalType.ON_NEXT, signal -> log.info("Confirm order ID: {} with payment intent ID: {}", orderId, paymentIntentId)))
                .flatMap(paymentResponse -> ordersCreator.getOrderByOrderId(orderId, paymentResponse.getStatus(), paymentIntentId))
                .flatMap(ordersCreator::saveOrdersAndReturnOrder)
                .flatMap(order -> orderProducer.sendOder(Mono.just(order)))
                .then(Mono.defer(() -> Mono.just(paymentResponseReceived.getPaymentResponse())));
    }

    public Mono<Void> saveOrder(Order order) {
        return ordersCreator.saveOrders(order)
                .doOnEach(withSpanInScope(SignalType.ON_NEXT, signal -> log.info("Save Order - ID: {}", order.getOrderKey().getOrderId())))
                .then(orderProducer.sendOder(Mono.just(order)))
                .then();
    }
}
