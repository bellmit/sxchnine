package com.project.business;

import com.project.mapper.OrderMapper;
import com.project.model.Order;
import com.project.model.Product;
import com.project.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
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
