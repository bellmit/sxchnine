package com.project.mapper;

import com.project.model.Order;
import com.project.model.OrderId;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OrderMapperTest {

    @Spy
    private OrderPrimaryKeyMapper orderPrimaryKeyMapper = Mappers.getMapper(OrderPrimaryKeyMapper.class);

    @InjectMocks
    private OrderMapper mapper = Mappers.getMapper(OrderMapper.class);

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testAsOrder(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        OrderId orderId = easyRandom.nextObject(OrderId.class);

        Order order = mapper.asOrder(orderId);

        assertThat(order).isEqualToIgnoringGivenFields(orderId, "orderPrimaryKey");

        assertThat(order.getOrderPrimaryKey()).isEqualToComparingFieldByFieldRecursively(orderId.getOrderIdPrimaryKey());

        verify(orderPrimaryKeyMapper).asOrderPrimaryKey(orderId.getOrderIdPrimaryKey());
    }

    @Test
    public void testAsOrderId(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        OrderId orderId = mapper.asOrderId(order);

        assertThat(orderId).isEqualToIgnoringGivenFields(order, "orderIdPrimaryKey");

        assertThat(orderId.getOrderIdPrimaryKey()).isEqualToComparingFieldByFieldRecursively(order.getOrderPrimaryKey());

        verify(orderPrimaryKeyMapper).asOrderIdPrimaryKey(order.getOrderPrimaryKey());
    }

    @Test
    public void testAsOrderNull(){
        assertThat(mapper.asOrder(null)).isNull();
    }

    @Test
    public void testAsOrderIdNull(){
        assertThat(mapper.asOrderId(null)).isNull();
    }
}
