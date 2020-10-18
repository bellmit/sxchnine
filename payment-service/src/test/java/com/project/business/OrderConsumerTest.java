package com.project.business;

import com.project.model.Order;
import org.jeasy.random.EasyRandom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import static com.project.utils.PaymentStatusCode.REFUSED;
import static com.project.utils.PaymentStatusCode.WAITING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderConsumerTest {

    @Mock
    private CatchupOrder catchupOrder;

    @InjectMocks
    private OrderConsumer orderConsumer;

    @Test
    public void testConsumeOrderWaitingPayment(){
        EasyRandom easyRandom = new EasyRandom();
        Order order = easyRandom.nextObject(Order.class);
        order.setPaymentStatus(WAITING.getValue());

        when(catchupOrder.catchUpCheckout(any(Order.class))).thenReturn(Mono.empty());

        orderConsumer.consumeOrder(order, () -> {});

        verify(catchupOrder).catchUpCheckout(order);
    }


    @Test
    public void testConsumeOrderRefusePayment(){
        EasyRandom easyRandom = new EasyRandom();
        Order order = easyRandom.nextObject(Order.class);
        order.setPaymentStatus(REFUSED.getValue());

        orderConsumer.consumeOrder(order, () -> {});

        verify(catchupOrder, times(0)).catchUpCheckout(order);
    }

    @Test(expected = NullPointerException.class)
    public void testConsumeOrderException(){
        orderConsumer.consumeOrder(null, ()->{});
    }
}
