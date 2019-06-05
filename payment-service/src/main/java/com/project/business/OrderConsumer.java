package com.project.business;

import com.project.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import static com.project.utils.PaymentStatusCode.WAITING;

@Service
@Slf4j
public class OrderConsumer {

    private final PaymentService paymentService;

    public OrderConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(groupId = "${kafka.consumer.groupId}", topics = "${kafka.consumer.topic}")
    public void consumeOrder(Order order, Acknowledgment acknowledgment) {
        log.info("***********************************************");
        log.info("***********************************************");
        log.info("consume order {} for payment", order.toString());
        log.info("***********************************************");
        log.info("***********************************************");

        if (order.getPaymentStatus().equalsIgnoreCase(WAITING.getValue())){
            paymentService.recheckout(order);
        }
        acknowledgment.acknowledge();
    }
}
