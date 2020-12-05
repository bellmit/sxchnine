package com.project.business;

import com.project.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.project.utils.PaymentStatusCode.*;

@Service
@Slf4j
public class OrderConsumer {

    @Autowired
    private List<EmailSender> emailSenderList;

    private final Map<String, EmailSender> context = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        emailSenderList.forEach(emailSender -> context.put(emailSender.type(), emailSender));
    }

    @KafkaListener(groupId = "${kafka.groupId}", topics = "${kafka.topic}")
    public void consumeOrder(Order order, Acknowledgment acknowledgment) {
        log.info("*************************************");
        log.info("**** Received: {}", order.toString());
        log.info("*************************************");
        context.get(order.getOrderStatus()).sendEmail(order);
        acknowledgment.acknowledge();
    }
}

