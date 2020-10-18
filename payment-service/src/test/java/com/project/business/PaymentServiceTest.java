package com.project.business;

import com.project.model.Order;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static com.project.utils.PaymentStatusCode.REFUSED;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .scanClasspathForConcreteTypes(true)
            .ignoreRandomizationErrors(true);


    @Test
    public void testRecheckoutPaymentRefused(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        //when(paymentServiceSpy.checkout(any(Order.class))).thenReturn(0);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        //paymentService.recheckout(order);

        assertThat(orderCaptor.getValue().getPaymentStatus()).isEqualTo(REFUSED.getValue());
    }

    @Test
    public void testRecheckoutPaymentWaitingAfterConfirmed(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        //paymentService.recheckout(order);
    }
}
