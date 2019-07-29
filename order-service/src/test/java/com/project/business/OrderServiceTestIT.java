package com.project.business;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.project.config.CassandraTestConfig;
import com.project.config.LocalRibbonClientConfigurationTest;
import com.project.config.ResourceServerConfigTest;
import com.project.model.Order;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.cassandraunit.spring.CassandraDataSet;
import org.cassandraunit.spring.CassandraUnitDependencyInjectionTestExecutionListener;
import org.cassandraunit.spring.CassandraUnitTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"spring.autoconfigure.exclude=" +
        "org.springframework.cloud.stream.test.binder.TestSupportBinderAutoConfiguration"})
@TestExecutionListeners(listeners = {
        CassandraUnitDependencyInjectionTestExecutionListener.class,
        CassandraUnitTestExecutionListener.class,
        ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class
})
@EmbeddedCassandra(timeout = 300000L)
@CassandraDataSet(value = {"schema.cql"}, keyspace = "test2")
@EmbeddedKafka(partitions = 3, topics = "products",
        brokerProperties = {
                "listeners=PLAINTEXT://127.0.0.1:51699"})
@Import({CassandraTestConfig.class, LocalRibbonClientConfigurationTest.class, ResourceServerConfigTest.class})
@ActiveProfiles("test")
@DirtiesContext
public class OrderServiceTestIT {

    private static final String ORDERS_QUEUE = "orders";

    @Autowired
    private OrderService orderService;

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, ORDERS_QUEUE);

    @ClassRule
    public static WireMockClassRule wireMockClassRule = new WireMockClassRule(9091);

    @Before
    public void setup(){
        System.setProperty("spring.embedded.kafka.brokers", embeddedKafka.getEmbeddedKafka().getBrokersAsString());
    }

    @Test
    public void testCheckoutOrderAndSave() throws Exception {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        stubFor(post(urlEqualTo("/pay"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("1")));

        Thread.sleep(1000L);

        int paymentStatus = orderService.checkoutOrderAndSave(order);

        assertThat(paymentStatus).isEqualTo(1);

        List<Order> orderByUserEmail = orderService.getOrderByUserEmail(order.getOrderPrimaryKey().getUserEmail());
        assertThat(orderByUserEmail.get(0)).isEqualToComparingFieldByFieldRecursively(order);

        Consumer kafkaConsumer = createKafkaConsumer();
        ConsumerRecord singleRecord = KafkaTestUtils.getSingleRecord(kafkaConsumer, ORDERS_QUEUE);

        assertThat((Order)singleRecord.value()).isEqualToComparingFieldByFieldRecursively(order);

    }

    private Consumer createKafkaConsumer() throws Exception {
        Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps("sender",
                "false", embeddedKafka.getEmbeddedKafka().kafkaPorts(51699));

        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JsonDeserializer<Order> jsonDeserializer = new JsonDeserializer<>(Order.class);
        jsonDeserializer.addTrustedPackages("*");
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);

        DefaultKafkaConsumerFactory<String, Order> consumerFactory = new DefaultKafkaConsumerFactory<>(
                consumerProperties);

        consumerFactory.setKeyDeserializer(new StringDeserializer());
        consumerFactory.setValueDeserializer(jsonDeserializer);
        Consumer<String, Order> consumer = consumerFactory.createConsumer();
        embeddedKafka.getEmbeddedKafka().consumeFromAllEmbeddedTopics(consumer);
        consumer.subscribe(Collections.singleton(ORDERS_QUEUE));
        return consumer;
    }
}
