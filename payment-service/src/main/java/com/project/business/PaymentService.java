package com.project.business;

import com.project.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.project.utils.PaymentStatusCode.CONFIRMED;
import static com.project.utils.PaymentStatusCode.REFUSED;

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
        if (checkoutStatus == 1){
            order.setPaymentStatus(CONFIRMED.getValue());
        } else if (checkoutStatus == 0){
            order.setPaymentStatus(REFUSED.getValue());
        } else if (checkoutStatus == 2){
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
