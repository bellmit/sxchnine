package com.project.business;

import com.project.client.PaymentServiceClient;
import com.project.model.Order;
import com.project.model.PaymentResponse;
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
        return orderRepository.findOrdersByOrderPrimaryKeyUserEmail(userEmail);
    }

    public Mono<PaymentResponse> checkoutOrderAndSave(Order order) {
        Mono<PaymentResponse> paymentStatus = paymentServiceClient.payOrder(order);

        return paymentStatus.map(i -> {
            order.setPaymentStatus(i.getStatus());
            return order;
        })
                .flatMap(ordersCreator::saveOrders)
                .then(orderProducer.sendOder(Mono.just(order)))
                .then(paymentStatus);
    }

    public Mono<Void> saveOrder(Order order) {
        return ordersCreator.saveOrders(order)
                .then(orderProducer.sendOder(Mono.just(order)))
                .then();
    }
}
