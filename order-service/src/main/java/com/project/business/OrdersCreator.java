package com.project.business;

import com.project.mapper.OrderMapper;
import com.project.model.Order;
import com.project.model.PaymentInfo;
import com.project.model.Product;
import com.project.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static com.project.utils.OrderHelper.evaluateStatus;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersCreator {

    private final OrderRepository orderRepository;
    private final OrderIdService orderIdService;
    private final OrderStatusService orderStatusService;
    private final OrderMapper orderMapper;


    public Mono<Void> saveOrders(Order order) {
        if (order != null) {
            order.setTotal(sumTotal(order));
            return orderRepository.save(order)
                    .then(orderIdService.saveOrderId(orderMapper.asOrderId(order)))
                    .then(orderStatusService.saveOrderStatus(orderMapper.asOrderStatusByOrder(order)));
        }
        return Mono.empty().then();
    }

    public Mono<Order> saveOrdersAndReturnOrder(Order order) {
        if (order != null) {
            order.setTotal(sumTotal(order));
            return orderRepository.save(order)
                    .then(orderIdService.saveOrderId(orderMapper.asOrderId(order)))
                    .then(orderStatusService.saveOrderStatus(orderMapper.asOrderStatusByOrder(order)))
                    .then(Mono.just(order));
        }
        return Mono.empty();
    }

    public Mono<Order> getOrderByOrderId(String orderId, String paymentStatus, String paymentIntentId){
        if (StringUtils.hasText(orderId)) {
            return orderIdService.getOrderByOrderId(orderId)
                    .map(retrievedOrder -> {
                        retrievedOrder.setPaymentStatus(paymentStatus);
                        retrievedOrder.setOrderStatus(evaluateStatus(paymentStatus));
                        if (retrievedOrder.getPaymentInfo() != null) {
                            retrievedOrder.getPaymentInfo().setPaymentIntentId(paymentIntentId);
                        } else {
                            retrievedOrder.setPaymentInfo(fallbackPaymentInfo(paymentIntentId));
                        }
                        return orderMapper.asOrder(retrievedOrder);
                    });
        } else {
            return Mono.empty();
        }
    }

    public Flux<Order> getOrderByOrderIdAndEmail(String orderId, String email){
        return orderIdService.getOrderByOrderIdAndEmail(orderId, email)
                .map(orderMapper::asOrder);
    }

    public Flux<Order> getOrderByOrderId(String orderId){
        return orderIdService.getOrderByOrderId(orderId)
                .map(orderMapper::asOrder)
                .flux();
    }

    private PaymentInfo fallbackPaymentInfo(String paymentIntentId){
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setPaymentIntentId(paymentIntentId);
        return paymentInfo;
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
