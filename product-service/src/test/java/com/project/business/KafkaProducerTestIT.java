package com.project.business;

import com.project.model.Product;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@ActiveProfiles("intg")
@EmbeddedKafka(topics = "products")
@DirtiesContext
@TestInstance(PER_CLASS)
public class KafkaProducerTestIT {

    private static String SENDER_TOPIC = "products";

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    public void testSend() {
        Product product = new Product();
        product.setId(11L);
        product.setName("BB");
        kafkaProducer.sendProduct(Mono.just(product)).block();

        Flux<ReceiverRecord<Integer, String>> kafkaConsumer = createKafkaConsumer();

        ReceiverRecord<Integer, String> receiverRecord = kafkaConsumer.blockFirst();

        assertTrue(receiverRecord.value().contains("BB"));
    }

    private Flux<ReceiverRecord<Integer, String>> createKafkaConsumer() {
        Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps(System.getProperty("spring.embedded.kafka.brokers"),
                "sender",
                "true");

        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        ReceiverOptions<Integer, String> receiverOptions =
                ReceiverOptions.<Integer, String>create(consumerProperties)
                        .subscription(Collections.singleton(SENDER_TOPIC));

        return KafkaReceiver.create(receiverOptions).receive();
    }


}

