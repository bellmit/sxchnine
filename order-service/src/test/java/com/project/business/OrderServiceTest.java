package com.project.business;

import com.project.client.PaymentServiceClient;
import com.project.model.Order;
import com.project.model.OrderId;
import com.project.model.PaymentResponse;
import com.project.producer.OrderProducer;
import com.project.repository.OrderRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentServiceClient paymentServiceClient;

    @Mock
    private OrderProducer orderProducer;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private OrderService orderService;

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @BeforeAll
    public static void init() {
        Hooks.resetOnEachOperator();
        Hooks.resetOnLastOperator();
        Schedulers.resetOnScheduleHooks();
    }

    @Test
    public void testCheckoutOrderAndSave() {
        Hooks.enableContextLossTracking();

        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(orderRepository.save(any())).thenReturn(Mono.empty());
        when(orderProducer.sendOder(any())).thenReturn(Mono.empty());

        PaymentResponse paymentResponse = easyRandom.nextObject(PaymentResponse.class);

        when(paymentServiceClient.payOrder(any(), any()))
                .thenReturn(Mono.just(paymentResponse));

        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(tracer.nextSpan()).thenReturn(span);

        PaymentResponse response = orderService.checkoutOrderAndSave(order)
                .contextWrite(ctx -> context)
                .block();

        assertThat(response.getOrderId()).isNull();

        verify(orderRepository).save(any());
        verify(orderProducer).sendOder(any());
    }

    @Test
    public void testSaveOrder() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(orderRepository.save(any())).thenReturn(Mono.empty());
        when(orderProducer.sendOder(any())).thenReturn(Mono.empty());

        StepVerifier.create(orderService.saveOrderAndSendToKafka(order))
                .expectComplete()
                .verify();

        verify(orderRepository).save(order);
        verify(orderProducer).sendOder(any());
    }

    @Test
    public void testGetOrderByUserEmail() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(orderRepository.findOrdersByUserEmail(anyString())).thenReturn(Flux.just(order));

        StepVerifier.create(orderService.getOrderByUserEmail("toto@gmail.com"))
                .expectNextMatches(o -> o.getUserEmail().equals(order.getUserEmail()))
                .expectComplete()
                .verify();
    }


    @Test
    public void testAllOrders() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(orderRepository.findAll()).thenReturn(Flux.just(order));

        StepVerifier.create(orderService.getAllOrders())
                .expectNextMatches(o -> o.getUserEmail().equals(order.getUserEmail()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetOrderByOrderId() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        System.out.println(ZonedDateTime.now());
        System.out.println(LocalDateTime.now());
        when(orderRepository.findOrderByOrderId(any())).thenReturn(Mono.just(order));

        StepVerifier.create(orderService.getOrderByOrderId(UUID.randomUUID().toString()))
                .expectNext(order)
                .expectComplete()
                .verify();
    }
}
