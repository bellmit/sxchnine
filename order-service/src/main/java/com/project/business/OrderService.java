package com.project.business;

import com.project.client.PaymentServiceClient;
import com.project.model.Order;
import com.project.producer.OrderProducer;
import com.project.repository.OrderRepository;
import com.project.utils.PaymentStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderService {

    private OrderRepository orderRepository;

    private OrdersCreator ordersCreator;

    private PaymentServiceClient paymentServiceClient;

    private OrderProducer orderProducer;

    public OrderService(OrderRepository orderRepository, OrdersCreator ordersCreator, PaymentServiceClient paymentServiceClient, OrderProducer orderProducer) {
        this.orderRepository = orderRepository;
        this.ordersCreator = ordersCreator;
        this.paymentServiceClient = paymentServiceClient;
        this.orderProducer = orderProducer;
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public List<Order> getOrderByUserEmail(String userEmail){
        return orderRepository.findOrdersByOrderPrimaryKeyUserEmail(userEmail);
    }

    public int checkoutOrderAndSave(Order order){
        int paymentStatus = paymentServiceClient.payOrder(order);
        log.info("payment : {}", paymentStatus);
        order.setPaymentStatus(PaymentStatusCode.getStatusByCode(paymentStatus));
        ordersCreator.saveOrders(order);
        orderProducer.sendOder(order);
        return paymentStatus;
    }

    public void saveOrder(Order order){
        ordersCreator.saveOrders(order);
        orderProducer.sendOder(order);
    }
}
