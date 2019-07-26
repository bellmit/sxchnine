package com.project.business;

import com.project.model.OrderId;
import com.project.repository.OrderByOrderIdRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderIdServiceTest {

    @Mock
    private OrderByOrderIdRepository orderByOrderIdRepository;

    @InjectMocks
    private OrderIdService orderIdService;

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testGetOrderByOrderId(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        OrderId orderId = easyRandom.nextObject(OrderId.class);

        when(orderByOrderIdRepository.findOrderIdByOrderIdPrimaryKeyOrderId(any())).thenReturn(orderId);

        OrderId orderByOrderId = orderIdService.getOrderByOrderId(UUID.randomUUID().toString());

        assertThat(orderByOrderId.equals(orderId));

    }

    @Test
    public void testSaveOrderId(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        OrderId orderId = easyRandom.nextObject(OrderId.class);

        when(orderByOrderIdRepository.save(any())).thenReturn(orderId);

        orderIdService.saveOrderId(orderId);

        verify(orderByOrderIdRepository).save(orderId);
    }

}
