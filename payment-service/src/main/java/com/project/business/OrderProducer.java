package com.project.business;

import com.project.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class OrderProducer {

    private final String topic;
    private final KafkaSender<String, Order> kafkaSender;
    private final Map<String, Order> catchupOrders = new ConcurrentHashMap<>();

    public OrderProducer(@Value("${kafka.producer.topic}") String topic, KafkaSender kafkaSender) {
        this.topic = topic;
        this.kafkaSender = kafkaSender;
    }

    public Mono<Order> sendOrder(Order order) {
        Mono<SenderRecord<String, Order, Object>> record = Mono.defer(() -> Mono.just(SenderRecord.create(topic, null, null, null, order, null)));
        return kafkaSender.send(record)
                .doOnNext(o -> log.info("Order sent successfully to kafka"))
                .doOnError(error -> {
                    log.error("error during sending catchup order {} to kafka - we will add it to list and send it later. ", order.getOrderId(), error);
                    catchupOrders.put(order.getOrderId(), order);
                })
                .doOnComplete(() -> log.info("Complete sending order to kafka"))
                .then(Mono.just(order));
    }


    @Scheduled(fixedDelay = 600000L)
    public void sendCatchupOrdersToKafka() {
        if (!catchupOrders.isEmpty()) {
            catchupOrders.forEach((k, v) -> sendOrder(v)
                    .subscribe(success -> {
                                log.info("Catchup Order {} sent successfully to Kafka", k);
                                catchupOrders.remove(k);
                            },
                            error -> log.error("error occurred during send order {} to kafka", k, error)));
        }
    }
}
