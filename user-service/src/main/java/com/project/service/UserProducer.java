package com.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@Slf4j
public class UserProducer {

    private final String topic;
    private final KafkaSender<Object, String> kafkaSender;
    private final ObjectMapper objectMapper;

    public UserProducer(@Value("${kafka.producer.user.topic}") String topic, KafkaSender<Object, String> kafkaSender, ObjectMapper objectMapper) {
        this.topic = topic;
        this.kafkaSender = kafkaSender;
        this.objectMapper = objectMapper;
    }

    public Mono<User> pushUserToKafka(User user){
        Mono<SenderRecord<Object, String, Object>>  userRecord = Mono.fromCallable(() -> user)
                .map(o -> SenderRecord.create(topic, null, null, null, mapOrder(o), null));

        return kafkaSender.send(userRecord)
                .doOnNext(o -> log.info("User: {} sent to kafka successfully", user.getEmail()))
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(1)))
                .doOnError(error -> log.warn("cannot send user: {} to kafka", user.getEmail(), error))
                .then(Mono.just(user));
    }

    private String mapOrder(User user) {
        try {
            return objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot serialize User for Kafka", e);
        }
    }
}
