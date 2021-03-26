package com.project.business;

import com.project.configuration.KafkaConfig;
import com.project.model.Order;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.project.utils.PaymentStatusCode.CHECKOUT_OP;
import static com.project.utils.PaymentStatusCode.WAITING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {OrderConsumer.class, KafkaConfig.class})
@EmbeddedKafka
@ActiveProfiles("test")
public class OrderConsumerTestIT {

    private static final String ORDERS_QUEUE = "orders";

    @MockBean
    private CatchupOrder catchupOrder;

    @Autowired
    private OrderConsumer orderConsumer;

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .scanClasspathForConcreteTypes(true)
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true);

    @Autowired
    public EmbeddedKafkaBroker embeddedKafkaRule;

    @BeforeEach
    public void setup() {
        System.setProperty("spring.embedded.kafka.brokers", embeddedKafkaRule.getBrokersAsString());
    }

    @Test
    public void testConsumeOrder() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);
        order.setPaymentStatus(CHECKOUT_OP.getValue());
        order.setOrderStatus(WAITING.getValue());


        ProducerRecord producerRecord = new ProducerRecord(ORDERS_QUEUE, "key", order);
        Producer producer = createProducer();
        producer.send(producerRecord);
        producer.close();

        when(catchupOrder.catchUpCheckout(any(Order.class))).thenReturn(Mono.just(order));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        orderConsumer.consumeOrder(order, () -> { });

        verify(catchupOrder).catchUpCheckout(orderCaptor.capture());

        assertThat(orderCaptor.getValue()).usingRecursiveComparison().isEqualTo(order);
    }

    private Producer createProducer() {
        Map<String, Object> config = KafkaTestUtils.producerProps(System.getProperty("spring.embedded.kafka.brokers"));
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaProducer(config);
    }
}
