package com.project.business;

import com.project.model.Order;
import org.jeasy.random.EasyRandom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.project.utils.PaymentStatusCode.REFUSED;
import static com.project.utils.PaymentStatusCode.WAITING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderConsumerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private OrderConsumer orderConsumer;

    @Test
    public void testConsumeOrderWaitingPayment(){
        EasyRandom easyRandom = new EasyRandom();
        Order order = easyRandom.nextObject(Order.class);
        order.setPaymentStatus(WAITING.getValue());

        doNothing().when(paymentService).recheckout(any(Order.class));

        orderConsumer.consumeOrder(order, () -> {});

        verify(paymentService).recheckout(order);
    }


    @Test
    public void testConsumeOrderRefusePayment(){
        EasyRandom easyRandom = new EasyRandom();
        Order order = easyRandom.nextObject(Order.class);
        order.setPaymentStatus(REFUSED.getValue());

        orderConsumer.consumeOrder(order, () -> {});

        verify(paymentService, times(0)).recheckout(order);
    }

    @Test(expected = NullPointerException.class)
    public void testConsumeOrderException(){
        orderConsumer.consumeOrder(null, ()->{});
    }
}
