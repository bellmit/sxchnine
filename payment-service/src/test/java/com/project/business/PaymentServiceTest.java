package com.project.business;

import com.project.model.Order;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(paymentOps.checkout(any(Order.class))).thenReturn(Mono.empty());

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        paymentService.checkout(order);

        verify(paymentOps).checkout(orderCaptor.capture());

    }

    @Test
    public void testCheckout3DSecure() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(paymentOps.checkout3DSecure(anyString())).thenReturn(Mono.empty());

        paymentService.checkout3DSecure(order.getPaymentInfo().getPaymentIntentId());

        verify(paymentOps).checkout3DSecure(order.getPaymentInfo().getPaymentIntentId());
    }
}
