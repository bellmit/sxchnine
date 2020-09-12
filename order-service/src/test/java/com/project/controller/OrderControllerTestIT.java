package com.project.controller;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@EmbeddedKafka
@TestPropertySource(properties = {"spring.autoconfigure.exclude=" +
        "org.springframework.cloud.stream.test.binder.TestSupportBinderAutoConfiguration"})
@TestExecutionListeners(listeners = {
        CassandraUnitDependencyInjectionTestExecutionListener.class,
        CassandraUnitTestExecutionListener.class,
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

    private ClientAndServer clientAndServer;

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .scanClasspathForConcreteTypes(true)
            .ignoreRandomizationErrors(true);

    @BeforeEach
    public void setup() {
        clientAndServer = ClientAndServer.startClientAndServer(9000);
    }

    @AfterEach
    public void teardown(){
        clientAndServer.stop();
    }

    @Test
    public void testSaveOrder() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order orderToSave = easyRandom.nextObject(Order.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSS");
        String format = LocalDateTime.now().format(formatter);

        orderToSave.setPaymentTime(LocalDateTime.parse(format));
        orderToSave.getOrderPrimaryKey().setOrderTime(LocalDateTime.parse(format));
        orderToSave.getOrderPrimaryKey().setShippingTime(LocalDateTime.parse(format));

        webTestClient.post().uri("/save").body(Mono.just(orderToSave), Order.class);

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
    public void testGetAllOrders() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order orderToSave = easyRandom.nextObject(Order.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss");
        String format = LocalDateTime.now().format(formatter);

        orderToSave.setPaymentTime(LocalDateTime.parse(format));
        orderToSave.getOrderPrimaryKey().setOrderTime(LocalDateTime.parse(format));
        orderToSave.getOrderPrimaryKey().setShippingTime(LocalDateTime.parse(format));

        orderRepository.save(orderToSave).block();

        webTestClient.get()
                .uri("/all")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class)
                .value(o -> assertThat(o.getOrderPrimaryKey().getOrderId()).isEqualTo(orderToSave.getOrderPrimaryKey().getOrderId()));
    }

    @Test
    public void testGetOrdersByOrderId() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        OrderId orderIdToSave = easyRandom.nextObject(OrderId.class);
        String uuid = randomUUID().toString();
        orderIdToSave.getOrderIdPrimaryKey().setOrderId(uuid);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss");
        String format = LocalDateTime.now().format(formatter);

        orderIdToSave.setPaymentTime(LocalDateTime.parse(format));
        orderIdToSave.getOrderIdPrimaryKey().setOrderTime(LocalDateTime.parse(format));
        orderIdToSave.getOrderIdPrimaryKey().setShippingTime(LocalDateTime.parse(format));

        orderByOrderIdRepository.save(orderIdToSave).block();

        webTestClient.get()
                .uri("/orderId/" + uuid)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(OrderId.class)
                .value(o -> assertThat(o.getOrderIdPrimaryKey().getOrderId()).isEqualTo(orderIdToSave.getOrderIdPrimaryKey().getOrderId()));
    }

    @Test
    public void testGetOrdersByEmail(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order orderToSave = easyRandom.nextObject(Order.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss");
        String format = LocalDateTime.now().format(formatter);

        orderToSave.setPaymentTime(LocalDateTime.parse(format));
        orderToSave.getOrderPrimaryKey().setOrderTime(LocalDateTime.parse(format));
        orderToSave.getOrderPrimaryKey().setShippingTime(LocalDateTime.parse(format));

        orderRepository.save(orderToSave).block();

        webTestClient.get()
                .uri("/userEmail/" + orderToSave.getOrderPrimaryKey().getUserEmail())
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class)
                .value(o -> assertThat(o.getOrderPrimaryKey().getOrderId()).isEqualTo(orderToSave.getOrderPrimaryKey().getOrderId()));
    }
}
