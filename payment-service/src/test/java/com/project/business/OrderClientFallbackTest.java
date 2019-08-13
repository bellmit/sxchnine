package com.project.business;

import com.project.model.Order;
import org.jeasy.random.EasyRandom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OrderClientFallbackTest {

    @Mock
    private OrderProducer orderProducer;

    @InjectMocks
    private OrderClientFallback orderClientFallback;

    @Test
    public void testSaveOrder(){
        EasyRandom easyRandom = new EasyRandom();
        Order order = easyRandom.nextObject(Order.class);

        doNothing().when(orderProducer).sendOrder(any(Order.class));

        orderClientFallback.saveOrder(order);

        verify(orderProducer).sendOrder(order);
    }

    @Test(expected = NullPointerException.class)
    public void testSaveOrderNullPointerException(){
        orderClientFallback.saveOrder(new Order());
    }
}
