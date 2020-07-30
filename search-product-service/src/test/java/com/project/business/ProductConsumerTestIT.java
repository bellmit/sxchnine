package com.project.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.Product;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@EmbeddedKafka(topics = "products")
@ActiveProfiles("test")
@DirtiesContext
@TestInstance(PER_CLASS)
@Testcontainers(disabledWithoutDocker = true)
public class ProductConsumerTestIT {

    private static final String PRODUCT_QUEUE = "products";

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafkaRule = new EmbeddedKafkaRule(1, true, PRODUCT_QUEUE);

    @BeforeAll
    public void setup(){
        System.setProperty("spring.embedded.kafka.brokers", embeddedKafkaRule.getEmbeddedKafka().getBrokersAsString());
    }

    @Test
    public void testConsumeProduct() throws InterruptedException, JsonProcessingException {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Product product = easyRandom.nextObject(Product.class);
        String jsonProduct = objectMapper.writeValueAsString(product);

        when(productService.save(any())).thenReturn(Mono.empty());

        Producer producer = createProducer(System.getProperty("spring.embedded.kafka.brokers"));
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(PRODUCT_QUEUE, jsonProduct);
        producer.send(producerRecord);
        producer.close();

        Thread.sleep(3000L);

        verify(productService).save(product);
    }


    public Producer createProducer(String address){
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(address);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        ProducerFactory<String, Product> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
        return producerFactory.createProducer();
    }

}
