package com.project.business;

import com.project.mapper.OrderMapper;
import com.project.model.Order;
import com.project.model.Product;
import com.project.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@Slf4j
public class OrdersCreator {

    private final OrderRepository orderRepository;

    private final OrderIdService orderIdService;

    private final OrderMapper orderMapper;

    public OrdersCreator(OrderRepository orderRepository, OrderIdService orderIdService, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderIdService = orderIdService;
        this.orderMapper = orderMapper;
    }

    public Mono<Void> saveOrders(Order order) {
        if (order != null) {
            return orderRepository.save(order)
                    .then(orderIdService.saveOrderId(orderMapper.asOrderId(order)));
        }
        return Mono.empty().then();
    }

    public Mono<Order> saveOrdersAndReturnOrder(Order order) {
        if (order != null) {
            return orderRepository.save(order)
                    .then(orderIdService.saveOrderId(orderMapper.asOrderId(order)))
                    .then(Mono.just(order));
        }
        return Mono.empty();
    }

    public Mono<Order> getOrderByOrderId(String orderId, String paymentStatus){
        if (StringUtils.hasText(orderId)) {
            return orderIdService.getOrderByOrderId(orderId)
                    .map(retrievedOrder -> {
                        retrievedOrder.setPaymentStatus(paymentStatus);
                        return orderMapper.asOrder(retrievedOrder);
                    });
        } else {
            return Mono.empty();
        }
    }


    private BigDecimal sumTotal(Order order) {
        if (order != null && !CollectionUtils.isEmpty(order.getProducts())) {
            return order.getProducts()
                    .stream()
                    .filter(p -> p != null && p.getUnitPrice() != null)
                    .map(Product::getUnitPrice)
                    .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }
}
