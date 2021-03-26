package com.project.controller;

import com.project.model.Order;
import com.project.repository.OrderRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.autoconfig.brave.BraveAutoConfiguration;
import org.springframework.cloud.sleuth.autoconfig.brave.instrument.web.client.BraveWebClientAutoConfiguration;
import org.springframework.cloud.sleuth.autoconfig.instrument.reactor.TraceReactorAutoConfiguration;
import org.springframework.cloud.sleuth.autoconfig.instrument.web.TraceWebAutoConfiguration;
import org.springframework.cloud.sleuth.autoconfig.zipkin2.ZipkinAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@EmbeddedKafka
@TestPropertySource(properties = {"spring.sleuth.web.client.enabled=false"})
@EnableAutoConfiguration(exclude = {TraceReactorAutoConfiguration.class,
        TraceWebAutoConfiguration.class,
        ZipkinAutoConfiguration.class,
        BraveAutoConfiguration.class,
        BraveWebClientAutoConfiguration.class
})
@DirtiesContext
public class OrderControllerTestIT {

    private static final String ORDERS_QUEUE = "orders";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private OrderRepository orderRepository;

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
    public void teardown() {
        clientAndServer.stop();
    }

    @Test
    public void testSaveOrder() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order orderToSave = easyRandom.nextObject(Order.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSS");
        String format = LocalDateTime.now().format(formatter);

        orderToSave.setPaymentTime(LocalDateTime.parse(format));
        orderToSave.setOrderTime(LocalDateTime.parse(format));
        orderToSave.setShippingTime(LocalDateTime.parse(format));

        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(tracer.nextSpan()).thenReturn(span);

        webTestClient.post()
                .uri("/save")
                .body(Mono.just(orderToSave), Order.class);

        Order savedOrder = orderRepository.findOrdersByUserEmail(orderToSave.getUserEmail()).blockFirst();

        assertThat(savedOrder).usingRecursiveComparison().ignoringFields("total", "paymentInfo", "address").isEqualTo(orderToSave);
        assertThat(savedOrder.getPaymentInfo()).usingRecursiveComparison().isEqualTo(orderToSave.getPaymentInfo());
        assertThat(savedOrder.getUserAddress()).usingRecursiveComparison().isEqualTo(orderToSave.getUserAddress());
    }

    @Test
    public void testGetOrdersByOrderId() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order orderToSave = easyRandom.nextObject(Order.class);
        String uuid = randomUUID().toString();
        orderToSave.setOrderId(uuid);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss");
        String format = LocalDateTime.now().format(formatter);

        orderToSave.setPaymentTime(LocalDateTime.parse(format));
        orderToSave.setOrderTime(LocalDateTime.parse(format));
        orderToSave.setShippingTime(LocalDateTime.parse(format));

        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(tracer.nextSpan()).thenReturn(span);

        orderRepository.save(orderToSave).block();

        webTestClient.get()
                .uri("/orderId/" + uuid)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class)
                .value(o -> assertThat(o.getOrderId()).isEqualTo(orderToSave.getOrderId()));
    }

    @Test
    public void testGetOrdersByEmail() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order orderToSave = easyRandom.nextObject(Order.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss");
        String format = LocalDateTime.now().format(formatter);

        orderToSave.setPaymentTime(LocalDateTime.parse(format));
        orderToSave.setOrderTime(LocalDateTime.parse(format));
        orderToSave.setShippingTime(LocalDateTime.parse(format));

        orderRepository.save(orderToSave).block();

        webTestClient.get()
                .uri("/userEmail/" + orderToSave.getUserEmail())
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class)
                .value(o -> assertThat(o.getOrderId()).isEqualTo(orderToSave.getOrderId()));
    }
}
