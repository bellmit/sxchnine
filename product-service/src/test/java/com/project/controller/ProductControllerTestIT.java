package com.project.controller;

import com.project.business.KafkaProducer;
import com.project.model.Product;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import utils.TestObjectCreator;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("intg")
@TestPropertySource(properties = {"application-intg.yml", "spring.autoconfigure.exclude=" +
        "org.springframework.cloud.stream.test.binder.TestSupportBinderAutoConfiguration"})
@EmbeddedKafka(partitions = 3,
        topics = "products",
        brokerProperties = {"listeners=PLAINTEXT://127.0.0.1:51699"})
@TestInstance(PER_CLASS)
@DirtiesContext
public class ProductControllerTestIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    EmbeddedKafkaBroker embeddedKafka;

    @BeforeAll
    public void setup(){
        Product product = TestObjectCreator.createProduct();
        mongoTemplate.save(product).subscribe();

        System.setProperty("spring.embedded.kafka.brokers", embeddedKafka.getBrokersAsString());
    }

    @Test
    public void testGetProductById(){
        webTestClient.get()
                .uri("/id/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Product.class)
                .value(Product::getId, equalTo(1L))
                .value(Product::getName, equalTo("p1"));
    }


    @Test
    public void testGetProductByName() {
        webTestClient.get()
                .uri("/name/p1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Product.class)
                .value(Product::getId, equalTo(1L))
                .value(Product::getName, equalTo("p1"));
    }


    @Test
    public void testGetAllProducts() {
        webTestClient.get()
                .uri("/all")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Product.class);
    }

    @AfterAll
    public void tearDown(){
        mongoTemplate.dropCollection("products");
    }

    private Consumer createKafkaConsumer() throws Exception {
        Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps("sender",
                "false", embeddedKafka.kafkaPorts(51699));

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
        embeddedKafka.consumeFromAllEmbeddedTopics(consumer);
        consumer.subscribe(Collections.singleton("products"));
        return consumer;
    }

}
