package com.project.consumer;

import com.project.business.OrderService;
import com.project.model.Order;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka
@TestPropertySource(properties = {"spring.autoconfigure.exclude=" +
        "org.springframework.cloud.stream.test.binder.TestSupportBinderAutoConfiguration"})
@DirtiesContext
public class OrderCatchupConsumerTestIT {

    private static final String ORDERS_QUEUE = "catchup-orders";

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderCatchupConsumer orderCatchupConsumer;

    EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testConsumeCatchupOrder() throws Exception {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        Producer producer = createProducer();
        ProducerRecord producerRecord = new ProducerRecord(ORDERS_QUEUE, order);
        producer.send(producerRecord);
        producer.close();

        Thread.sleep(1000L);

        orderCatchupConsumer.consumeCatchupOrder(order, () -> {
        });

        orderService.getOrderByUserEmail(order.getUserEmail())
                .subscribe(ordersSaved -> Assertions.assertThat(ordersSaved).usingRecursiveComparison().isEqualTo(order));

    }

    public Producer createProducer() {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(System.getProperty("spring.embedded.kafka.brokers"));
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        ProducerFactory<String, Order> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
        return producerFactory.createProducer();
    }

}
