package com.project.business;

import com.project.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderProducer {

    private final String topic;
    private final KafkaTemplate kafkaTemplate;

    public OrderProducer(@Value("${kafka.producer.topic}") String topic, KafkaTemplate kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrder(Order order){
        log.info("send order {} to catchup with status {}", order.getOrderPrimaryKey().getOrderId(), order.getPaymentStatus());
        kafkaTemplate.send(topic, order);
    }
}
