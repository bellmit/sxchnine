package com.project.mapper;

import com.project.model.Order;
import com.project.model.OrderId;
import com.project.model.OrderStatus;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OrderMapperTest {

    @Spy
    private OrderKeyMapper orderKeyMapper = Mappers.getMapper(OrderKeyMapper.class);

    @Spy
    private OrderStatusKeyMapper orderStatusKeyMapper = Mappers.getMapper(OrderStatusKeyMapper.class);

    @InjectMocks
    private final OrderMapper mapper = Mappers.getMapper(OrderMapper.class);

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testAsOrderByOrderId(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        OrderId orderId = easyRandom.nextObject(OrderId.class);

        Order order = mapper.asOrder(orderId);

        assertThat(order).isEqualToIgnoringGivenFields(orderId, "orderKey");

        assertThat(order.getOrderKey()).usingRecursiveComparison().isEqualTo(orderId.getOrderIdKey());

        verify(orderKeyMapper).asOrderPrimaryKey(orderId.getOrderIdKey());
    }

    @Test
    public void testAsOrderByOrderStatus(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        OrderStatus orderStatus = easyRandom.nextObject(OrderStatus.class);

        Order order = mapper.asOrderByOrderStatus(orderStatus);

        assertThat(order).isEqualToIgnoringGivenFields(orderStatus, "orderKey");

        assertThat(order.getOrderKey()).usingRecursiveComparison().isEqualTo(orderStatus.getOrderStatusKey());

        verify(orderStatusKeyMapper).asOrderPrimaryKey(orderStatus.getOrderStatusKey());
    }

    @Test
    public void testAsOrderId(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        OrderId orderId = mapper.asOrderId(order);

        assertThat(orderId).isEqualToIgnoringGivenFields(order, "orderIdKey");
        assertThat(orderId.getOrderIdKey()).usingRecursiveComparison().isEqualTo(order.getOrderKey());

        verify(orderKeyMapper).asOrderIdPrimaryKey(order.getOrderKey());
    }

    @Test
    public void testAsOrderStatus(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        OrderStatus orderStatus = mapper.asOrderStatusByOrder(order);

        assertThat(orderStatus).isEqualToIgnoringGivenFields(order, "orderStatusKey");
        assertThat(orderStatus.getOrderStatusKey()).isEqualToIgnoringGivenFields(order.getOrderKey(), "bucket");
        assertThat(orderStatus.getOrderStatusKey().getBucket()).isEqualTo(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")));

        verify(orderStatusKeyMapper).asOrderStatusPrimaryKeyByOrder(order.getOrderKey());
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
