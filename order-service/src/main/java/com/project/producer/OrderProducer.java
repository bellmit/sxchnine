package com.project.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Service
@RefreshScope
@Slf4j
public class OrderProducer {

    private final String topic;
    private final KafkaSender<Object, String> kafkaSender;
    private final ObjectMapper objectMapper;

    public OrderProducer(@Value("${kafka.producer.topic}") String topic, KafkaSender kafkaSender, ObjectMapper objectMapper) {
        this.topic = topic;
        this.kafkaSender = kafkaSender;
        this.objectMapper = objectMapper;
    }

    public Mono<Void> sendOder(Mono<Order> order){
        Mono<SenderRecord<Object, String, Object>> recordMono = order
                .map(o -> SenderRecord.create(topic, null, null, null, mapOrder(o), null))
                .subscribeOn(Schedulers.boundedElastic());

        return kafkaSender.send(recordMono)
                .doOnNext(o -> log.info("{} sent to kafka successfully", o.recordMetadata().toString()))
                .then();
    }

    private String mapOrder(Order order){
        try {
            return objectMapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
