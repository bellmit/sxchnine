package com.project.business;

import com.project.mapper.OrderMapper;
import com.project.model.Order;
import com.project.model.OrderId;
import com.project.repository.OrderRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;
import reactor.util.context.Context;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdersCreatorTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderIdService orderIdService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderStatusService orderStatusService;

    @InjectMocks
    private OrdersCreator ordersCreator;

    private static final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testSaveOrders() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);
        OrderId orderId = easyRandom.nextObject(OrderId.class);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        when(orderRepository.save(order)).thenReturn(Mono.just(order));
        when(orderMapper.asOrderId(order)).thenReturn(orderId);
        when(orderIdService.saveOrderId(orderId)).thenReturn(Mono.just(orderId).then());
        when(orderStatusService.saveOrderStatus(any())).thenReturn(Mono.empty());

        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(tracer.nextSpan()).thenReturn(span);

        StepVerifierOptions stepVerifierOptions = StepVerifierOptions
                .create()
                .withInitialContext(context);

        StepVerifier.create(ordersCreator.saveOrders(order), stepVerifierOptions)
                .expectComplete()
                .verify();

        verify(orderMapper).asOrderId(orderCaptor.capture());
        verify(orderIdService).saveOrderId(orderId);
        verify(orderRepository).save(order);
    }

    @Test
    public void testSaveOrderNullOrder() {
        StepVerifier.create(ordersCreator.saveOrders(null))
                .expectComplete()
                .verify();

        verify(orderMapper, times(0)).asOrderId(new Order());
    }

    @Test
    public void testSaveOrderNullOrderWithoutProducts() {
        StepVerifier.create(ordersCreator.saveOrders(null))
                .expectComplete()
                .verify();

        verify(orderRepository, never()).save(any());
        verify(orderIdService, never()).saveOrderId(any());
        verify(orderMapper, never()).asOrderId(any());
    }
}
