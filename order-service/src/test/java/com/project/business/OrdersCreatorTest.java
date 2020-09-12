package com.project.business;

import com.project.mapper.OrderMapper;
import com.project.model.Order;
import com.project.model.OrderId;
import com.project.repository.OrderRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrdersCreatorTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderIdService orderIdService;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrdersCreator ordersCreator;

    private static final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testSaveOrders(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);
        OrderId orderId = easyRandom.nextObject(OrderId.class);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        ArgumentCaptor<OrderId> orderIdCaptor = ArgumentCaptor.forClass(OrderId.class);

        when(orderRepository.save(order)).thenReturn(Mono.just(order));
        when(orderMapper.asOrderId(order)).thenReturn(orderId);
        when(orderIdService.saveOrderId(orderId)).thenReturn(Mono.just(orderId).then());

        StepVerifier.create(ordersCreator.saveOrders(order))
                .expectComplete()
                .verify();

        verify(orderMapper).asOrderId(orderCaptor.capture());
        verify(orderIdService).saveOrderId(orderId);
        verify(orderRepository).save(order);
    }

    @Test
    public void testSaveOrderNullOrder(){
        StepVerifier.create(ordersCreator.saveOrders(null))
                .expectComplete()
                .verify();

        verify(orderMapper, times(0)).asOrderId(new Order());
    }

    @Test
    public void testSaveOrderNullOrderWithoutProducts(){
        StepVerifier.create(ordersCreator.saveOrders(null))
                .expectComplete()
                .verify();

        verify(orderRepository, never()).save(any());
        verify(orderIdService, never()).saveOrderId(any());
        verify(orderMapper, never()).asOrderId(any());
    }
}
