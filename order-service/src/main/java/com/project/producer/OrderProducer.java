package com.project.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Service
@RefreshScope
@Slf4j
public class OrderProducer {

    private final String topic;
    private final KafkaSender<Object, String> kafkaSender;
    private final ObjectMapper objectMapper;
    private final Set<Mono<Order>> backupOrders = new HashSet<>();

    public OrderProducer(@Value("${kafka.producer.topic}") String topic, KafkaSender kafkaSender, ObjectMapper objectMapper) {
        this.topic = topic;
        this.kafkaSender = kafkaSender;
        this.objectMapper = objectMapper;
    }

    public Mono<Void> sendOder(Mono<Order> order) {
        Mono<SenderRecord<Object, String, Object>> recordMono = order
                .doOnNext(o -> log.info("order to sent {}", o.toString()))
                .map(o -> SenderRecord.create(topic, null, null, null, mapOrder(o), null))
                .subscribeOn(Schedulers.boundedElastic());

        return kafkaSender.send(recordMono)
                .doOnNext(o -> log.info("sent to kafka successfully"))
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(1)))
                .doOnError(error -> {
                    backupOrders.add(order);
                    log.warn("cannot send order to kafka - will try later", error);
                })
                .then();
    }

    private String mapOrder(Order order) {
        try {
            return objectMapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Scheduled(cron = "0 */30 * ? * *")
    public void catchupOrders() {
        if (!CollectionUtils.isEmpty(backupOrders)) {
            Flux.fromStream(backupOrders.stream())
                    .flatMap(this::sendOder)
                    .subscribe(success -> log.info("send backup orders to kafka"),
                            error -> log.error("error occurred while sending back up orders - it will be sent on the next batch", error),
                            () -> {
                                log.info("Back up orders successfully sent to kafka - we will clear the list");
                                backupOrders.clear();
                            });
        }

    }
}
