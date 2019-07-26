package com.project.business;

import com.project.mapper.OrderMapper;
import com.project.model.Order;
import com.project.model.OrderId;
import com.project.model.Product;
import com.project.repository.OrderRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
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

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testSaveOrders(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);
        OrderId orderId = easyRandom.nextObject(OrderId.class);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        when(orderMapper.asOrderId(order)).thenReturn(orderId);

        ordersCreator.saveOrders(order);

        verify(orderMapper).asOrderId(orderCaptor.capture());
        verify(orderIdService).saveOrderId(orderId);
        verify(orderRepository).save(order);

        assertThat(orderCaptor.getValue().getTotal())
                .isEqualByComparingTo(order.getProducts()
                        .stream()
                        .map(Product::getUnitPrice)
                        .reduce(BigDecimal::add)
                        .get());
    }

    @Test
    public void testSaveOrderNullOrder(){
        ordersCreator.saveOrders(null);

        verify(orderMapper, times(0)).asOrderId(new Order());
    }

    @Test
    public void testSaveOrderNullOrderWithoutProducts(){
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        ordersCreator.saveOrders(new Order());

        verify(orderMapper).asOrderId(orderCaptor.capture());
        verify(orderMapper, times(0)).asOrderId(new Order());

        assertThat(orderCaptor.getValue().getTotal()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
