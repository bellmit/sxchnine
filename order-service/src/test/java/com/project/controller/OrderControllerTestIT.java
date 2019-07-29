package com.project.controller;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.project.config.CassandraTestConfig;
import com.project.config.LocalRibbonClientConfigurationTest;
import com.project.config.ResourceServerConfigTest;
import com.project.model.Order;
import com.project.model.OrderId;
import com.project.repository.OrderByOrderIdRepository;
import com.project.repository.OrderRepository;
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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@EmbeddedKafka(topics = "orders",
        brokerProperties = {
                "listeners=PLAINTEXT://127.0.0.1:51699"})
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
@Import({ResourceServerConfigTest.class, CassandraTestConfig.class, LocalRibbonClientConfigurationTest.class})
@DirtiesContext
public class OrderControllerTestIT {

    private static final String ORDERS_QUEUE = "orders";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderByOrderIdRepository orderByOrderIdRepository;

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .scanClasspathForConcreteTypes(true)
            .ignoreRandomizationErrors(true);

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafkaRule = new EmbeddedKafkaRule(1, true, ORDERS_QUEUE);

    @ClassRule
    public static WireMockClassRule wireMockClassRule = new WireMockClassRule(WireMockConfiguration.options().port(9091));

    @Before
    public void setup(){
        System.setProperty("spring.embedded.kafka.brokers", embeddedKafkaRule.getEmbeddedKafka().getBrokersAsString());
    }

    @Test
    public void testSaveOrder() throws InterruptedException {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order orderToSave = easyRandom.nextObject(Order.class);
        Thread.sleep(1000L);

        testRestTemplate.postForEntity("/save", orderToSave, Order.class);

        List<Order> savedOrders = orderRepository
                .findOrdersByOrderPrimaryKeyUserEmail(orderToSave.getOrderPrimaryKey().getUserEmail());
        OrderId savedOrderId = orderByOrderIdRepository
                .findOrderIdByOrderIdPrimaryKeyOrderId(orderToSave.getOrderPrimaryKey().getOrderId());

        assertThat(savedOrders.get(0)).isEqualToIgnoringGivenFields(orderToSave, "total", "paymentInfo", "address");
        assertThat(savedOrders.get(0).getPaymentInfo()).isEqualToIgnoringGivenFields(orderToSave.getPaymentInfo());
        assertThat(savedOrders.get(0).getUserAddress()).isEqualToIgnoringGivenFields(orderToSave.getUserAddress());
        assertThat(savedOrderId.getOrderIdPrimaryKey()).isEqualToComparingFieldByField(orderToSave.getOrderPrimaryKey());
        assertThat(savedOrderId.getPaymentInfo()).isEqualToComparingFieldByField(orderToSave.getPaymentInfo());
        assertThat(savedOrderId.getUserAddress()).isEqualToComparingFieldByField(orderToSave.getUserAddress());
    }

    @Test
    public void testGetAllOrders() throws InterruptedException {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order orderToSave = easyRandom.nextObject(Order.class);

        Thread.sleep(1000L);

        orderRepository.save(orderToSave);

        ResponseEntity<List<Order>> response = testRestTemplate
                .exchange("/all",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Order>>() {
                        });

        assertThat(response.getBody().get(0)).isNotNull();
    }

    @Test
    public void testGetOrdersByOrderId() throws InterruptedException {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        OrderId orderIdToSave = easyRandom.nextObject(OrderId.class);
        String uuid = randomUUID().toString();
        orderIdToSave.getOrderIdPrimaryKey().setOrderId(UUID.fromString(uuid));

        Thread.sleep(1000L);

        orderByOrderIdRepository.save(orderIdToSave);

        OrderId orderId = testRestTemplate.getForObject("/orderId/" + uuid, OrderId.class);

        assertThat(orderId).isEqualToComparingFieldByFieldRecursively(orderIdToSave);
    }

    @Test
    public void testGetOrdersByEmail() throws InterruptedException, IOException {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order orderToSave = easyRandom.nextObject(Order.class);

        Thread.sleep(1000L);

        orderRepository.save(orderToSave);

        ResponseEntity<List<Order>> response = testRestTemplate
                .exchange("/userEmail/" + orderToSave.getOrderPrimaryKey().getUserEmail(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Order>>() {});

        assertThat(response.getBody().get(0)).isEqualToComparingFieldByFieldRecursively(orderToSave);

    }
}
