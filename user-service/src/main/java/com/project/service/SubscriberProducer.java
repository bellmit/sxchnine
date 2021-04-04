package com.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.Subscription;
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
public class SubscriberProducer {

    private final String topic;
    private final KafkaSender<Object, String> kafkaSender;
    private final ObjectMapper objectMapper;

    public SubscriberProducer(@Value("${kafka.producer.subscriber.topic}") String topic, KafkaSender<Object, String> kafkaSender, ObjectMapper objectMapper) {
        this.topic = topic;
        this.kafkaSender = kafkaSender;
        this.objectMapper = objectMapper;
    }

    public Mono<Subscription> pushSubscriberToKafka(Subscription subscription) {
        Mono<SenderRecord<Object, String, Object>> subscriptionRecord = Mono.fromCallable(() -> subscription)
                .map(s -> SenderRecord.create(topic, null, null, null, mapSubscription(s), null));

        return kafkaSender.send(subscriptionRecord)
                .doOnNext(o -> log.info("Subscriber: {} sent to kafka successfully", subscription.getEmail()))
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(1)))
                .doOnError(error -> log.warn("cannot send Subscriber: {} to kafka", subscription.getEmail(), error))
                .then(Mono.just(subscription));
    }

    private String mapSubscription(Subscription subscription) {
        try {
            return objectMapper.writeValueAsString(subscription);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot serialize Subscription for Kafka", e);
        }
    }
}
