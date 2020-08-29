package com.project.controller;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.project.config.CassandraTestConfig;
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
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.SocketUtils;

import java.io.IOException;
import java.net.ServerSocket;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@EmbeddedKafka
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
@Import({CassandraTestConfig.class})
@DirtiesContext
public class OrderControllerTestIT {

    private static final String ORDERS_QUEUE = "orders";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderByOrderIdRepository orderByOrderIdRepository;

    private static final int port = SocketUtils.findAvailableTcpPort();

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .scanClasspathForConcreteTypes(true)
            .ignoreRandomizationErrors(true);

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafkaRule = new EmbeddedKafkaRule(1, true, ORDERS_QUEUE);

    @ClassRule
    public static WireMockClassRule wireMockClassRule = new WireMockClassRule(port);

    @BeforeClass
    public static void setup() {
        System.setProperty("wiremock.server.port", String.valueOf(port));
    }

    @Test
    public void testSaveOrder() throws InterruptedException {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order orderToSave = easyRandom.nextObject(Order.class);
        Thread.sleep(1000L);

        webTestClient.post().uri("/save").body(orderToSave, Order.class);

        orderRepository.findOrdersByOrderPrimaryKeyUserEmail(orderToSave.getOrderPrimaryKey().getUserEmail())
                .subscribe(savedOrder -> {
                    assertThat(savedOrder).isEqualToIgnoringGivenFields(orderToSave, "total", "paymentInfo", "address");
                    assertThat(savedOrder.getPaymentInfo()).isEqualToIgnoringGivenFields(orderToSave.getPaymentInfo());
                    assertThat(savedOrder.getUserAddress()).isEqualToIgnoringGivenFields(orderToSave.getUserAddress());
                });

        orderByOrderIdRepository.findOrderIdByOrderIdPrimaryKeyOrderId(orderToSave.getOrderPrimaryKey().getOrderId())
                .subscribe(savedOrderId -> {
                    assertThat(savedOrderId.getOrderIdPrimaryKey()).isEqualToComparingFieldByField(orderToSave.getOrderPrimaryKey());
                    assertThat(savedOrderId.getPaymentInfo()).isEqualToComparingFieldByField(orderToSave.getPaymentInfo());
                    assertThat(savedOrderId.getUserAddress()).isEqualToComparingFieldByField(orderToSave.getUserAddress());
                });


    }

    @Test
    public void testGetAllOrders() throws InterruptedException {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order orderToSave = easyRandom.nextObject(Order.class);

        Thread.sleep(1000L);

        orderRepository.save(orderToSave).block();

        webTestClient.get()
                .uri("/all")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class)
                .isEqualTo(orderToSave);
    }

    @Test
    public void testGetOrdersByOrderId() throws InterruptedException {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        OrderId orderIdToSave = easyRandom.nextObject(OrderId.class);
        String uuid = randomUUID().toString();
        orderIdToSave.getOrderIdPrimaryKey().setOrderId(uuid);

        Thread.sleep(1000L);

        orderByOrderIdRepository.save(orderIdToSave).block();

        webTestClient.get()
                .uri("/orderId/" + uuid)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(OrderId.class)
                .isEqualTo(orderIdToSave);
    }

    @Test
    public void testGetOrdersByEmail() throws InterruptedException, IOException {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order orderToSave = easyRandom.nextObject(Order.class);

        Thread.sleep(1000L);

        orderRepository.save(orderToSave).block();

        webTestClient.get()
                .uri("/userEmail/" + orderToSave.getOrderPrimaryKey().getUserEmail())
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class)
                .isEqualTo(orderToSave);
    }

    public static int findRandomPort() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            return serverSocket.getLocalPort();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
