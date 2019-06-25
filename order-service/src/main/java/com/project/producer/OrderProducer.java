package com.project.producer;

import com.project.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderProducer {

    private final String topic;
    private final KafkaTemplate kafkaTemplate;

    public OrderProducer(@Value("${kafka.producer.topic}") String topic, KafkaTemplate kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOder(Order order){
        kafkaTemplate.send(topic, order);
    }
}
