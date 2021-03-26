package com.project.business;

import com.project.model.Order;
import com.project.model.PaymentResponse;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentOps paymentOps;

    @InjectMocks
    private PaymentService paymentService;

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .scanClasspathForConcreteTypes(true)
            .ignoreRandomizationErrors(true);


    @Test
    public void testCheckoutPayment() {
        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(tracer.nextSpan()).thenReturn(span);

        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        PaymentResponse paymentResponse = easyRandom.nextObject(PaymentResponse.class);

        when(paymentOps.checkout(any(Order.class))).thenReturn(Mono.just(paymentResponse));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        StepVerifierOptions stepVerifierOptions = StepVerifierOptions
                .create()
                .withInitialContext(context);

        StepVerifier.create(paymentService.checkout(order), stepVerifierOptions)
                .expectNext(paymentResponse)
                .expectComplete()
                .verify();

        verify(paymentOps).checkout(orderCaptor.capture());

    }

    @Test
    public void testCheckout3DSecure() {
        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(tracer.nextSpan()).thenReturn(span);

        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        PaymentResponse paymentResponse = easyRandom.nextObject(PaymentResponse.class);

        when(paymentOps.checkout3DSecure(anyString())).thenReturn(Mono.just(paymentResponse));

        StepVerifierOptions stepVerifierOptions = StepVerifierOptions
                .create()
                .withInitialContext(context);

        StepVerifier.create(paymentService.checkout3DSecure(order.getPaymentInfo().getPaymentIntentId()), stepVerifierOptions)
                .expectNext(paymentResponse)
                .expectComplete()
                .verify();

        verify(paymentOps).checkout3DSecure(order.getPaymentInfo().getPaymentIntentId());
    }
}
