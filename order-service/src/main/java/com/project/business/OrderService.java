package com.project.business;

import com.project.client.PaymentServiceClient;
import com.project.model.Order;
import com.project.producer.OrderProducer;
import com.project.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.project.utils.PaymentStatusCode.getStatusByCode;

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

    public Mono<Integer> checkoutOrderAndSave(Order order) {
        Mono<Integer> paymentStatus = paymentServiceClient.payOrder(order);

        return paymentStatus.map(i -> {
            order.setPaymentStatus(getStatusByCode(i));
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
