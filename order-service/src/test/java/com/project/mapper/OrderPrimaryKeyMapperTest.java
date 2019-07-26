package com.project.mapper;

import com.project.model.OrderIdPrimaryKey;
import com.project.model.OrderPrimaryKey;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class OrderPrimaryKeyMapperTest {

    private OrderPrimaryKeyMapper mapper = Mappers.getMapper(OrderPrimaryKeyMapper.class);

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testAsOrderPrimaryKey(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        OrderIdPrimaryKey orderIdPrimaryKey = easyRandom.nextObject(OrderIdPrimaryKey.class);

        OrderPrimaryKey orderPrimaryKey = mapper.asOrderPrimaryKey(orderIdPrimaryKey);

        assertThat(orderPrimaryKey).isEqualToComparingFieldByFieldRecursively(orderIdPrimaryKey);
    }

    @Test
    public void testAsOrderIdPrimaryKey(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        OrderPrimaryKey orderPrimaryKey = easyRandom.nextObject(OrderPrimaryKey.class);

        OrderIdPrimaryKey orderIdPrimaryKey = mapper.asOrderIdPrimaryKey(orderPrimaryKey);

        assertThat(orderIdPrimaryKey).isEqualToComparingFieldByFieldRecursively(orderPrimaryKey);
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
