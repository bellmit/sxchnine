package com.project.business;

import com.project.model.Product;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest(classes = {KafkaProducer.class, KafkaAutoConfiguration.class})
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
        product.setId("AA");
        product.setName("BB");
        kafkaProducer.sendProduct(product);

        Consumer kafkaConsumer = createKafkaConsumer();
        ConsumerRecord singleRecord = KafkaTestUtils.getSingleRecord(kafkaConsumer, SENDER_TOPIC);

        assertEquals("AA", ((Product) singleRecord.value()).getId());
        assertEquals("BB", ((Product) singleRecord.value()).getName());

    }

    private Consumer createKafkaConsumer() throws Exception {
        Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps("sender",
                "false", embeddedKafka.getEmbeddedKafka().kafkaPorts(51697));

        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JsonDeserializer<Product> jsonDeserializer = new JsonDeserializer<>(Product.class);
        jsonDeserializer.addTrustedPackages("*");
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);

        DefaultKafkaConsumerFactory<String, Product> consumerFactory = new DefaultKafkaConsumerFactory<>(
                consumerProperties);

        consumerFactory.setKeyDeserializer(new StringDeserializer());
        consumerFactory.setValueDeserializer(jsonDeserializer);
        Consumer<String, Product> consumer = consumerFactory.createConsumer();
        embeddedKafka.getEmbeddedKafka().consumeFromAllEmbeddedTopics(consumer);
        consumer.subscribe(Collections.singleton(SENDER_TOPIC));
        return consumer;
    }

}

