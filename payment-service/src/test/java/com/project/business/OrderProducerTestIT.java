package com.project.business;

import com.project.configuration.KafkaConfig;
import com.project.model.Order;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {OrderProducer.class, KafkaConfig.class, KafkaAutoConfiguration.class})
@EmbeddedKafka
@ActiveProfiles("test")
@DirtiesContext
public class OrderProducerTestIT {

    private static String CATCHUP_QUEUE = "catchup-orders";

    @Autowired
    private OrderProducer orderProducer;

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .ignoreRandomizationErrors(true)
            .collectionSizeRange(0, 2)
            .scanClasspathForConcreteTypes(true);

    @Autowired
    public EmbeddedKafkaBroker embeddedKafkaRule;

    @BeforeEach
    public void setup() {
        System.setProperty("spring.embedded.kafka.brokers", embeddedKafkaRule.getBrokersAsString());
    }

    @Test
    public void testSendOrder() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);
        order.getOrderKey().setOrderTime(LocalDateTime.now().withNano(0));
        order.setPaymentTime(LocalDateTime.now().withNano(0));
        order.setShippingTime(LocalDateTime.now().withNano(0));

        orderProducer.sendOrder(order).block();

        Consumer consumer = createConsumer();
        consumer.subscribe(Collections.singletonList(CATCHUP_QUEUE));
        ConsumerRecord record = KafkaTestUtils.getSingleRecord(consumer, CATCHUP_QUEUE);

        assertThat((Order) record.value()).usingRecursiveComparison().isEqualTo(order);
    }

    private Consumer createConsumer() {
        Map<String, Object> config = KafkaTestUtils.consumerProps(System.getProperty("spring.embedded.kafka.brokers"),
                "consumerGroup1", "true");

        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JsonDeserializer jsonDeserializer = new JsonDeserializer();
        jsonDeserializer.addTrustedPackages("*");

        DefaultKafkaConsumerFactory<String, Order> consumerFactory = new DefaultKafkaConsumerFactory(config);
        consumerFactory.setKeyDeserializer(new StringDeserializer());
        consumerFactory.setValueDeserializer(jsonDeserializer);
        return consumerFactory.createConsumer();
    }
}
