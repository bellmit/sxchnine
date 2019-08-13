package com.project.business;

import com.project.config.ElasticsearchConfiguration;
import com.project.model.Product;
import com.project.repository.ProductRepository;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
@EmbeddedKafka(topics = "products")
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.autoconfigure.exclude=" +
        "org.springframework.cloud.stream.test.binder.TestSupportBinderAutoConfiguration"})
@DirtiesContext
@Import(ElasticsearchConfiguration.class)
public class ProductConsumerTestIT {

    private static final String PRODUCT_QUEUE = "products";

    @MockBean
    private ProductRepository productRepository;

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafkaRule = new EmbeddedKafkaRule(1, true, PRODUCT_QUEUE);

    @Test
    public void testConsumeProduct() throws InterruptedException {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Product product = easyRandom.nextObject(Product.class);

        Producer producer = createProducer(System.getProperty("spring.embedded.kafka.brokers"));
        ProducerRecord producerRecord = new ProducerRecord(PRODUCT_QUEUE, product);
        producer.send(producerRecord);
        producer.close();

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);

        Thread.sleep(1000L);
        verify(productRepository).save(captor.capture());
    }


    public Producer createProducer(String address){
        Map<String, Object> producerProps = KafkaTestUtils.senderProps(address);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        ProducerFactory<String, Product> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
        return producerFactory.createProducer();
    }

}
