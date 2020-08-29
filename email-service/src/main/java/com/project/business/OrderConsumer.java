package com.project.business;

import com.project.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import static com.project.utils.PaymentStatusCode.*;

@Service
@Slf4j
public class OrderConsumer {

    private final EmailConfirmationSender emailConfirmationSender;

    private final EmailPendingSender emailPendingSender;

    private final EmailRefusedSender emailRefusedSender;

    public OrderConsumer(EmailConfirmationSender emailConfirmationSender, EmailPendingSender emailPendingSender, EmailRefusedSender emailRefusedSender) {
        this.emailConfirmationSender = emailConfirmationSender;
        this.emailPendingSender = emailPendingSender;
        this.emailRefusedSender = emailRefusedSender;
    }

    @KafkaListener(groupId = "${kafka.groupId}", topics = "${kafka.topic}")
    public void consumeOrder(Order order, Acknowledgment acknowledgment) {
        log.info("*************************************");
        log.info("**** Received: {}", order.toString());
        log.info("*************************************");
        if (order.getPaymentStatus().equalsIgnoreCase(CONFIRMED.getValue())) {
            emailConfirmationSender.sendEmail(order);
        }
        if (order.getPaymentStatus().equalsIgnoreCase(WAITING.getValue())) {
            emailPendingSender.sendEmail(order);
        }
        if (order.getPaymentStatus().equalsIgnoreCase(REFUSED.getValue())){
            emailRefusedSender.sendEmail(order);
        }
        acknowledgment.acknowledge();
    }
}

