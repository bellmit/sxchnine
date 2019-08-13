package com.project.business;

import com.project.model.Order;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.project.utils.PaymentStatusCode.CONFIRMED;
import static com.project.utils.PaymentStatusCode.REFUSED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest {

    @Mock
    private OrderClient orderClient;

    private PaymentService paymentService;

    private PaymentService paymentServiceSpy;

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .scanClasspathForConcreteTypes(true)
            .ignoreRandomizationErrors(true);

    @Before
    public void setup(){
        paymentService = new PaymentService(orderClient);

        paymentServiceSpy = spy(paymentService);
    }

    @Test
    public void testRecheckoutPaymentConfirmed(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(paymentServiceSpy.checkout(any(Order.class))).thenReturn(1);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        paymentServiceSpy.recheckout(order);

        verify(orderClient).saveOrder(orderCaptor.capture());

        assertThat(orderCaptor.getValue().getPaymentStatus()).isEqualTo(CONFIRMED.getValue());
    }


    @Test
    public void testRecheckoutPaymentRefused(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(paymentServiceSpy.checkout(any(Order.class))).thenReturn(0);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        paymentServiceSpy.recheckout(order);

        verify(orderClient).saveOrder(orderCaptor.capture());

        assertThat(orderCaptor.getValue().getPaymentStatus()).isEqualTo(REFUSED.getValue());
    }

    @Test
    public void testRecheckoutPaymentWaitingAfterConfirmed(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(paymentServiceSpy.checkout(any(Order.class))).thenReturn(2, 1);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        paymentServiceSpy.recheckout(order);

        verify(paymentServiceSpy, times(2)).recheckout(orderCaptor.capture());

        verify(orderClient, times(2)).saveOrder(order);

        assertThat(orderCaptor.getValue().getPaymentStatus()).isEqualTo(CONFIRMED.getValue());
    }
}
