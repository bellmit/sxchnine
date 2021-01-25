package com.project.business;

import com.project.client.PaymentServiceClient;
import com.project.model.Order;
import com.project.model.PaymentResponse;
import com.project.model.PaymentResponseWrapper;
import com.project.producer.OrderProducer;
import com.project.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.project.utils.OrderHelper.evaluateStatus;
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
        return orderRepository.findOrdersByOrderKeyUserEmail(userEmail);
    }

    public Flux<Order> trackOrder(String orderId, String email){
        MDC.put("orderId", orderId);
        if (hasText(orderId) && hasText(email)){
            return ordersCreator.getOrderByOrderIdAndEmail(orderId, email);
        } else if (hasText(orderId) && !hasText(email)){
            return ordersCreator.getOrderByOrderId(orderId);
        } else if (!hasText(orderId) && hasText(email)){
            return getOrderByUserEmail(email);
        }
        return Flux.empty();
    }

    public Mono<PaymentResponse> checkoutOrderAndSave(Order order) {
        PaymentResponseWrapper paymentResponseReceived = new PaymentResponseWrapper();
        MDC.put("orderId", order.getOrderKey().getOrderId());
        return paymentServiceClient.payOrder(order, paymentResponseReceived)
                .map(paymentResponse -> {
                    order.setPaymentStatus(paymentResponse.getStatus());
                    order.setOrderStatus(evaluateStatus(paymentResponse.getStatus()));
                    order.getPaymentInfo().setPaymentIntentId(paymentResponse.getPaymentIntentId());
                    return order;
                })
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
        MDC.put("orderId", orderId);
        return paymentServiceClient.confirmPay(paymentIntentId, paymentResponseReceived)
                .flatMap(paymentResponse -> ordersCreator.getOrderByOrderId(orderId, paymentResponse.getStatus(), paymentIntentId))
                .flatMap(ordersCreator::saveOrdersAndReturnOrder)
                .flatMap(order -> orderProducer.sendOder(Mono.just(order)))
                .then(Mono.defer(() -> Mono.just(paymentResponseReceived.getPaymentResponse())));
    }

    public Mono<Void> saveOrder(Order order) {
        MDC.put("orderId", order.getOrderKey().getOrderId());
        return ordersCreator.saveOrders(order)
                .then(orderProducer.sendOder(Mono.just(order)))
                .then();
    }
}
