package com.project.business;

import com.project.configuration.KafkaConfig;
import com.project.model.Order;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
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

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafkaRule = new EmbeddedKafkaRule(1, true, CATCHUP_QUEUE);

    @BeforeClass
    public static void setup(){
        System.setProperty("spring.embedded.kafka.brokers", embeddedKafkaRule.getEmbeddedKafka().getBrokersAsString());
    }

    @Test
    public void testSendOrder(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        orderProducer.sendOrder(order);

        Consumer consumer = createConsumer();
        consumer.subscribe(Collections.singletonList(CATCHUP_QUEUE));
        ConsumerRecord record = KafkaTestUtils.getSingleRecord(consumer, CATCHUP_QUEUE);

        assertThat((Order)record.value()).isEqualToComparingFieldByFieldRecursively(order);
    }

    private Consumer createConsumer(){
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
