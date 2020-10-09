package com.project.controller;

import com.project.business.OrderIdService;
import com.project.business.OrderService;
import com.project.model.Order;
import com.project.model.OrderId;
import com.project.model.PaymentResponse;
import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.project.utils.PaymentStatusCode.CONFIRMED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = OrderController.class,
        excludeAutoConfiguration = {ValidationAutoConfiguration.class})
public class OrderControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderIdService orderIdService;

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testGetAllOrders() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(orderService.getAllOrders()).thenReturn(Flux.just(order));

        webTestClient.get()
                .uri("/all")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class)
                .value(o -> Assertions.assertThat(o).usingRecursiveComparison().isEqualTo(order));

        verify(orderService).getAllOrders();
    }

    @Test
    public void testGetOrdersByOrderId() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        OrderId orderId = easyRandom.nextObject(OrderId.class);

        when(orderIdService.getOrderByOrderId(anyString())).thenReturn(Mono.just(orderId));

        webTestClient.get()
                .uri("/orderId/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(OrderId.class)
                .value(o -> Assertions.assertThat(o).usingRecursiveComparison().isEqualTo(orderId));

        verify(orderIdService).getOrderByOrderId("1");
    }

    @Test
    public void testGetOrdersByEmail() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(orderService.getOrderByUserEmail(anyString())).thenReturn(Flux.just(order));

        webTestClient.get()
                .uri("/userEmail/toto@gmail.com")
                .accept(MediaType.APPLICATION_STREAM_JSON)
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
                .isEqualTo(paymentResponse);

        verify(orderService).checkoutOrderAndSave(orderCaptor.capture());
    }


    @Test
    public void testSave() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(orderService.saveOrder(any())).thenReturn(Mono.just(order).then());

        ArgumentCaptor<Order> captorOrder = ArgumentCaptor.forClass(Order.class);

        webTestClient.post()
                .uri("/save")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(order), Order.class)
                .exchange()
                .expectStatus().is2xxSuccessful();

        verify(orderService).saveOrder(captorOrder.capture());
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
