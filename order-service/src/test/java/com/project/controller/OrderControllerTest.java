package com.project.controller;

import com.project.business.OrderService;
import com.project.business.OrderStatusService;
import com.project.model.Order;
import com.project.model.PaymentResponse;
import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.time.LocalDateTime;

import static com.project.utils.PaymentStatusCode.CONFIRMED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@WebFluxTest(controllers = OrderController.class,
        excludeAutoConfiguration = {ValidationAutoConfiguration.class})
public class OrderControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderStatusService orderStatusService;

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Disabled
    @Test
    public void testGetOrdersByOrderId() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);
        order.setOrderTime(LocalDateTime.now().withNano(0));
        order.setPaymentTime(LocalDateTime.now().withNano(0));
        order.setShippingTime(LocalDateTime.now().withNano(0));

        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(tracer.nextSpan()).thenReturn(span);

        when(orderService.getOrderByOrderId(anyString())).thenReturn(Mono.just(order));

        webTestClient.get()
                .uri("/orderId/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class)
                .value(o -> Assertions.assertThat(o).usingRecursiveComparison().isEqualTo(order));

        verify(orderService).getOrderByOrderId("1");
    }

    @Disabled
    @Test
    public void testGetOrdersByEmail() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);
        order.setOrderTime(LocalDateTime.now().withNano(0));
        order.setPaymentTime(LocalDateTime.now().withNano(0));
        order.setShippingTime(LocalDateTime.now().withNano(0));

        when(orderService.getOrderByUserEmail(anyString())).thenReturn(Flux.just(order));

        webTestClient.get()
                .uri("/userEmail/toto@gmail.com")
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class)
                .value(o -> Assertions.assertThat(o).usingRecursiveComparison().isEqualTo(order));

        verify(orderService).getOrderByUserEmail("toto@gmail.com");
    }


    @Test
    public void testCheckoutOrderAndSave() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setStatus(CONFIRMED.getValue());

        when(orderService.checkoutOrderAndSave(any())).thenReturn(Mono.just(paymentResponse));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        webTestClient.post()
                .uri("/checkoutOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(order), Order.class)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(PaymentResponse.class)
                .value(pr -> assertThat(pr).usingRecursiveComparison().isEqualTo(paymentResponse));

        verify(orderService).checkoutOrderAndSave(orderCaptor.capture());
    }

    @Test
    public void testSave() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(orderService.saveOrderAndSendToKafka(any())).thenReturn(Mono.just(order).then());

        ArgumentCaptor<Order> captorOrder = ArgumentCaptor.forClass(Order.class);

        webTestClient.post()
                .uri("/save")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(order), Order.class)
                .exchange()
                .expectStatus().is2xxSuccessful();

        verify(orderService).saveOrderAndSendToKafka(captorOrder.capture());
    }

    @Test
    public void testNotFound() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        webTestClient.post().uri("/saved")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(order), Order.class)
                .exchange()
                .expectStatus().is4xxClientError();
    }
}
