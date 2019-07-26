package com.project.business;

import com.project.client.PaymentServiceClient;
import com.project.model.Order;
import com.project.producer.OrderProducer;
import com.project.repository.OrderRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrdersCreator ordersCreator;

    @Mock
    private PaymentServiceClient paymentServiceClient;

    @Mock
    private OrderProducer orderProducer;

    @InjectMocks
    private OrderService orderService;

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testCheckoutOrderAndSave(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        when(paymentServiceClient.payOrder(order)).thenReturn(1);

        orderService.checkoutOrderAndSave(order);

        verify(ordersCreator).saveOrders(orderCaptor.capture());
        verify(orderProducer).sendOder(orderCaptor.capture());

        assertThat(orderCaptor.getValue().getPaymentStatus()).isEqualTo("CONFIRMED");

    }


    @Test
    public void testSaveOrder(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        orderService.saveOrder(order);

        verify(ordersCreator).saveOrders(order);
        verify(orderProducer).sendOder(order);
    }

    @Test
    public void testGetOrderByUserEmail(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(orderRepository.findOrdersByOrderPrimaryKeyUserEmail(anyString())).thenReturn(Collections.singletonList(order));

        List<Order> orderByUserEmail = orderService.getOrderByUserEmail("toto@gmail.com");

        assertThat(orderByUserEmail.get(0)).isEqualTo(order);
    }


    @Test
    public void testAllOrders(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        List<Order> orders = orderService.getAllOrders();

        assertThat(orders.get(0)).isEqualTo(order);
    }
}
