package com.project.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Component
@RefreshScope
@Slf4j
public class KafkaProducer {

    @Value("${kafka.topic}")
    private String topic;

    private KafkaSender kafkaSender;

    private ObjectMapper mapper;

    public KafkaProducer(KafkaSender kafkaSender, ObjectMapper objectMapper) {
        this.kafkaSender = kafkaSender;
        this.mapper = objectMapper;
    }

    public Mono<Product> sendProduct(Mono<Product> product){
        Mono<SenderRecord<Object, String, Object>> recordMono = product
                .map(p -> SenderRecord.create(topic, null, null, null, mapProduct(p), null));

        return kafkaSender.send(recordMono)
                .doOnNext(p -> log.info("{} sent to kafka successfully", p.toString()))
                .then(product);
    }

    private String mapProduct(Product product){
        try {
            return mapper.writeValueAsString(product);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
