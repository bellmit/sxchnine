package com.project.business;

import com.project.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@Slf4j
public class KafkaProducer {

    @Value("${kafka.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendProduct(Product product){
        try {
            kafkaTemplate.send(topic, product);

        } catch (Throwable e){
            log.warn("Can't send to topic", e);
        }
    }
}
