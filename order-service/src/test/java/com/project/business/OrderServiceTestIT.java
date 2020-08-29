package com.project.business;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.project.config.CassandraTestConfig;
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
import org.junit.BeforeClass;
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
import org.springframework.util.SocketUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.project.utils.PaymentStatusCode.CONFIRMED;
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
@EmbeddedKafka
@Import({CassandraTestConfig.class})
@ActiveProfiles("test")
@DirtiesContext
public class OrderServiceTestIT {

    private static final String ORDERS_QUEUE = "orders";

    @Autowired
    private OrderService orderService;

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    private static final int port = SocketUtils.findAvailableTcpPort();

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, ORDERS_QUEUE);

    @ClassRule
    public static WireMockClassRule wireMockClassRule = new WireMockClassRule(port);

    @BeforeClass
    public static void setup(){
        System.setProperty("wiremock.server.port", String.valueOf(port));
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

        orderService.checkoutOrderAndSave(order)
                .subscribe(paymentStatus -> assertThat(paymentStatus).isEqualTo(1));


        orderService.getOrderByUserEmail(order.getOrderPrimaryKey().getUserEmail())
                .subscribe(orderByEmail -> {
                    assertThat(orderByEmail).usingRecursiveComparison().isEqualTo(order);
                    assertThat(orderByEmail.getPaymentStatus()).isEqualTo(CONFIRMED.getValue());
                });


        Consumer kafkaConsumer = createKafkaConsumer();
        ConsumerRecord singleRecord = KafkaTestUtils.getSingleRecord(kafkaConsumer, ORDERS_QUEUE);
        kafkaConsumer.close();

        assertThat((Order)singleRecord.value()).usingRecursiveComparison().isEqualTo(order);

    }

    private Consumer createKafkaConsumer() throws Exception {
        Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps(System.getProperty("spring.embedded.kafka.brokers"),
                "false", "true");

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
        consumer.subscribe(Collections.singleton(ORDERS_QUEUE));
        return consumer;
    }

    public static int findRandomPort(){
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            return serverSocket.getLocalPort();

        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
