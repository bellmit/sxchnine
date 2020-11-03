package com.project.mapper;

import com.project.model.OrderIdKey;
import com.project.model.OrderKey;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class OrderKeyMapperTest {

    private OrderKeyMapper mapper = Mappers.getMapper(OrderKeyMapper.class);

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testAsOrderPrimaryKey(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        OrderIdKey orderIdKey = easyRandom.nextObject(OrderIdKey.class);

        OrderKey orderKey = mapper.asOrderPrimaryKey(orderIdKey);

        assertThat(orderKey.getOrderTime()).isEqualTo(orderIdKey.getOrderTime());
        assertThat(orderKey.getUserEmail()).isEqualTo(orderIdKey.getUserEmail());
    }

    @Test
    public void testAsOrderIdPrimaryKey(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        OrderKey orderKey = easyRandom.nextObject(OrderKey.class);

        OrderIdKey orderIdKey = mapper.asOrderIdPrimaryKey(orderKey);

        assertThat(orderKey.getOrderTime()).isEqualTo(orderIdKey.getOrderTime());
        assertThat(orderKey.getUserEmail()).isEqualTo(orderIdKey.getUserEmail());
    }

    @Test
    public void testAsOrderPrimaryKeyNull(){
        assertThat(mapper.asOrderPrimaryKey(null)).isNull();
    }

    @Test
    public void testAsOrderIdPrimaryKeyNull(){
        assertThat(mapper.asOrderIdPrimaryKey(null)).isNull();
    }

}
