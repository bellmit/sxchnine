package com.project.business;

import com.project.mapper.OrderMapper;
import com.project.model.Order;
import com.project.model.Product;
import com.project.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;

@Service
public class OrdersCreator {

    private OrderRepository orderRepository;

    private OrderIdService orderIdService;

    private OrderMapper orderMapper;

    public OrdersCreator(OrderRepository orderRepository, OrderIdService orderIdService, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderIdService = orderIdService;
        this.orderMapper = orderMapper;
    }

    public void saveOrders(Order order) {
        if (order != null) {
            order.setTotal(sumTotal(order));
            orderRepository.save(order);
            orderIdService.saveOrderId(orderMapper.asOrderId(order));
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
