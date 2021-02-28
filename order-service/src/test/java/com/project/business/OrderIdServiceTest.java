package com.project.business;

import com.project.model.OrderId;
import com.project.repository.OrderByOrderIdRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;
import reactor.util.context.Context;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
public class OrderIdServiceTest {

    @Mock
    private OrderByOrderIdRepository orderByOrderIdRepository;

    @InjectMocks
    private OrderIdService orderIdService;

    private static final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testGetOrderByOrderId() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        OrderId orderId = easyRandom.nextObject(OrderId.class);

        when(orderByOrderIdRepository.findOrderIdByOrderIdKeyOrderId(any())).thenReturn(Mono.just(orderId));

        StepVerifier.create(orderIdService.getOrderByOrderId(UUID.randomUUID().toString()))
                .expectNext(orderId)
                .expectComplete()
                .verify();
    }

    @Test
    public void testSaveOrderId() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        OrderId orderId = easyRandom.nextObject(OrderId.class);

        when(orderByOrderIdRepository.save(any())).thenReturn(Mono.just(orderId));

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

        StepVerifier.create(orderIdService.saveOrderId(orderId), stepVerifierOptions)
                .expectComplete()
                .verify();

        verify(orderByOrderIdRepository).save(orderId);
    }

}
