package com.project.business;

import com.project.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Service
@Slf4j
public class OrderProducer {

    private final String topic;
    private final KafkaSender<String, Order> kafkaSender;

    public OrderProducer(@Value("${kafka.producer.topic}") String topic, KafkaSender kafkaSender) {
        this.topic = topic;
        this.kafkaSender = kafkaSender;
    }

    public Mono<Void> sendOrder(Order order){
        Mono<SenderRecord<String, Order, Object>> record = Mono.defer(() -> Mono.just(SenderRecord.create(topic, null, null, null, order, null)));
        return kafkaSender.send(record)
                .doOnNext(o -> log.info("Order sent successfully to kafka"))
                .then();
    }
}
