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
        if (!orderId.isBlank() && !email.isBlank()){
            return ordersCreator.getOrderByOrderIdAndEmail(orderId, email);
        } else if (!orderId.isBlank() && email.isBlank()){
            return ordersCreator.getOrderByOrderId(orderId);
        } else if (orderId.isBlank() && !email.isBlank()){
            return getOrderByUserEmail(email);
        }
        return Flux.empty();
    }

    public Mono<PaymentResponse> checkoutOrderAndSave(Order order) {
        PaymentResponseWrapper paymentResponseReceived = new PaymentResponseWrapper();
        return paymentServiceClient.payOrder(order, paymentResponseReceived)
                .map(i -> {
                    order.setPaymentStatus(i.getStatus());
                    return order;
                })
                .flatMap(ordersCreator::saveOrders)
                .then(orderProducer.sendOder(Mono.just(order)))
                .then(Mono.defer(() -> Mono.just(paymentResponseReceived.getPaymentResponse() != null ? paymentResponseReceived.getPaymentResponse() : new PaymentResponse())));
    }

    public Mono<PaymentResponse> confirmOrderAndSave(String paymentIntentId, String orderId) {
        PaymentResponseWrapper paymentResponseReceived = new PaymentResponseWrapper();
        return paymentServiceClient.confirmPay(paymentIntentId, paymentResponseReceived)
                .flatMap(paymentResponse -> ordersCreator.getOrderByOrderId(orderId, paymentResponse.getStatus()))
                .flatMap(ordersCreator::saveOrdersAndReturnOrder)
                .flatMap(order -> orderProducer.sendOder(Mono.just(order)))
                .then(Mono.defer(() -> Mono.just(paymentResponseReceived.getPaymentResponse())));
    }

    public Mono<Void> saveOrder(Order order) {
        return ordersCreator.saveOrders(order)
                .then(orderProducer.sendOder(Mono.just(order)))
                .then();
    }
}
