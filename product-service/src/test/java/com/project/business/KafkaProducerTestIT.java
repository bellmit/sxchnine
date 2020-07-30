package com.project.business;

import com.project.model.Product;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
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
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@ActiveProfiles("intg")
@TestPropertySource(properties = {"application-intg.yml", "spring.autoconfigure.exclude=" +
        "org.springframework.cloud.stream.test.binder.TestSupportBinderAutoConfiguration"})
@EmbeddedKafka(partitions = 3, topics = "products",
        brokerProperties = {
                "listeners=PLAINTEXT://127.0.0.1:51697"})
@DirtiesContext
@TestInstance(PER_CLASS)
public class KafkaProducerTestIT {

    private static String SENDER_TOPIC = "products";

    @Autowired
    private KafkaProducer kafkaProducer;

    @ClassRule
    EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, SENDER_TOPIC);

    @BeforeAll
    public void setUp() throws Exception {
        System.setProperty("spring.embedded.kafka.brokers", embeddedKafka.getEmbeddedKafka().getBrokersAsString());
    }

    @Test
    public void testSend() throws Exception {
        Product product = new Product();
        product.setId(11L);
        product.setName("BB");
        kafkaProducer.sendProduct(Mono.just(product));

        Flux<ReceiverRecord<Integer, String>> kafkaConsumer = createKafkaConsumer();

        kafkaConsumer.subscribe(p -> assertTrue(p.value().contains("BB")));
    }

    private Flux<ReceiverRecord<Integer, String>> createKafkaConsumer() throws Exception {
        Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps("sender",
                "false", embeddedKafka.getEmbeddedKafka().kafkaPorts(51697));

        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        ReceiverOptions<Integer, String> receiverOptions =
                ReceiverOptions.<Integer, String>create(consumerProperties)
                        .subscription(Collections.singleton(SENDER_TOPIC));

        return KafkaReceiver.create(receiverOptions).receive();
    }


}

