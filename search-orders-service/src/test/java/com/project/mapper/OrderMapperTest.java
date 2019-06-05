package com.project.mapper;

import com.project.model.IndexedOrder;
import com.project.model.Order;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderMapperTest {

    @Spy
    private UuidMapper uuidMapper;

    @InjectMocks
    private OrderMapper mapper = Mappers.getMapper(OrderMapper.class);

    @Test
    public void testAsIndexedOrders() {
        EasyRandomParameters easyRandomParameters = new EasyRandomParameters().collectionSizeRange(1, 4);
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        List<IndexedOrder> orders = mapper.asIndexedOrders(order);

        assertEquals(3, orders.size());
        assertEquals(orders.get(0).getOrderId(), order.getOrderPrimaryKey().getOrderId().toString());
        assertEquals(orders.get(0).getOrderTime(), order.getOrderPrimaryKey().getOrderTime().toString());
        assertEquals(orders.get(0).getUserEmail(), order.getOrderPrimaryKey().getUserEmail());
        assertEquals(orders.get(0).getShippingTime(), order.getOrderPrimaryKey().getShippingTime().toString());
        assertEquals(orders.get(0).getId(), orders.get(0).getOrderId()+orders.get(0).getProductId());
    }
}