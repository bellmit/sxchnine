package com.project.business;

import com.project.exception.PaymentMethodException;
import com.project.model.Order;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static com.project.utils.PaymentStatusCode.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderConsumerTest {

    @Mock
    private CatchupOrder catchupOrder;

    @InjectMocks
    private OrderConsumer orderConsumer;

    @Test
    public void testConsumeOrderWaitingPayment() {
        EasyRandom easyRandom = new EasyRandom();
        Order order = easyRandom.nextObject(Order.class);
        order.setPaymentStatus(CHECKOUT_OP.getValue());
        order.setOrderStatus(WAITING.getValue());

        Order orderProcessing = easyRandom.nextObject(Order.class);
        orderProcessing.setProcessingStatus(WAITING_TIMEOUT.getValue());

        when(catchupOrder.catchUpCheckout(any(Order.class))).thenReturn(Mono.just(orderProcessing));

        assertThrows(PaymentMethodException.class, () -> orderConsumer.consumeOrder(order, () -> { }));

        verify(catchupOrder).catchUpCheckout(order);
    }


    @Test
    public void testConsumeOrderRefusePayment() {
        EasyRandom easyRandom = new EasyRandom();
        Order order = easyRandom.nextObject(Order.class);
        order.setPaymentStatus(REFUSED.getValue());

        orderConsumer.consumeOrder(order, () -> {});

        verify(catchupOrder, times(0)).catchUpCheckout(order);
    }

    @Test
    public void testConsumeOrderException() {
        assertThrows(NullPointerException.class, () -> orderConsumer.consumeOrder(null, () -> {}));
    }
}
