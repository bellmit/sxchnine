package com.project.business;

import com.project.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.project.utils.PaymentStatusCode.*;

@Service
@Slf4j
public class PaymentService {

    private OrderClient orderClient;

    public PaymentService(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    public int checkout(Order order){
        log.info("checkout order {}", order.getOrderPrimaryKey().getOrderId().toString());
        //TODO: call external payment API
        return 0;
    }

    public void recheckout(Order order){
        int checkoutStatus = checkout(order);
        if (checkoutStatus == CONFIRMED.getCode()){
            order.setPaymentStatus(CONFIRMED.getValue());
        } else if (checkoutStatus == REFUSED.getCode()){
            order.setPaymentStatus(REFUSED.getValue());
        } else if (checkoutStatus == WAITING.getCode()){
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            recheckout(order);
        }
        orderClient.saveOrder(order);
    }
}
