package com.project.business;

import brave.Tracing;
import brave.propagation.StrictCurrentTraceContext;
import brave.sampler.Sampler;
import com.project.client.PaymentServiceClient;
import com.project.model.Order;
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
import org.springframework.cloud.sleuth.brave.bridge.BraveCurrentTraceContext;
import org.springframework.cloud.sleuth.brave.bridge.CompositeSpanHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.context.Context;
import reactor.util.context.ContextView;

import java.util.Optional;

import static com.project.utils.PaymentStatusCode.CONFIRMED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrdersCreator ordersCreator;

    @Mock
    private PaymentServiceClient paymentServiceClient;

    @Mock
    private OrderProducer orderProducer;

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

        when(ordersCreator.saveOrders(any())).thenReturn(Mono.empty());
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

        verify(ordersCreator).saveOrders(any());
        verify(orderProducer).sendOder(any());
    }

    @Test
    public void testSaveOrder() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(ordersCreator.saveOrders(any())).thenReturn(Mono.empty());
        when(orderProducer.sendOder(any())).thenReturn(Mono.empty());

        StepVerifier.create(orderService.saveOrder(order))
                .expectComplete()
                .verify();

        verify(ordersCreator).saveOrders(order);
        verify(orderProducer).sendOder(any());
    }

    @Test
    public void testGetOrderByUserEmail() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(orderRepository.findOrdersByOrderKeyUserEmail(anyString())).thenReturn(Flux.just(order));

        Order order1 = orderService.getOrderByUserEmail("toto@gmail.com")
                .blockFirst();

        assertThat(order1.getOrderKey().getOrderId()).isEqualTo(order.getOrderKey().getOrderId());
    }



    @Test
    public void testAllOrders() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(orderRepository.findAll()).thenReturn(Flux.just(order));

        StepVerifier.create(orderService.getAllOrders())
                .expectNextMatches(o -> o.getOrderKey().getUserEmail().equals(order.getOrderKey().getUserEmail()))
                .expectComplete()
                .verify();
    }
}
