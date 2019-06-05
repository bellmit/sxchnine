package com.project.business;

import com.project.mapper.OrderMapper;
import com.project.model.Order;
import com.project.model.Product;
import com.project.repository.OrderRepository;
import org.springframework.stereotype.Service;

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

    public void saveOrders(Order order){
        order.setTotal(sumTotal(order));
        orderIdService.saveOrderId(orderMapper.asOrderId(order));
        orderRepository.save(order);
    }


    private BigDecimal sumTotal(Order order) {
        return order.getProducts().stream().map(Product::getUnitPrice).reduce(BigDecimal.valueOf(0), BigDecimal::add);
    }
}
