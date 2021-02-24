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
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class OrderConsumer {

    @Autowired
    private List<EmailSender> emailSenderList;

    private final Map<String, EmailSender> context = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        emailSenderList.forEach(emailSender -> context.put(emailSender.type(), emailSender));
    }

    @KafkaListener(groupId = "${kafka.order.groupId}",
            topics = "${kafka.order.topic}",
            containerFactory = "ordersKafkaListenerContainerFactory")
    public void consumeOrder(Order order, Acknowledgment acknowledgment) {
        log.info("*************************************");
        log.info("**** Order Received: {}", order.toString());
        log.info("*************************************");

        Optional.ofNullable(context.get(order.getOrderStatus())).ifPresent(sender -> sender.sendEmail(order));
        acknowledgment.acknowledge();
    }
}

