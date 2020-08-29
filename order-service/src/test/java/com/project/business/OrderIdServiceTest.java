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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderIdServiceTest {

    @Mock
    private OrderByOrderIdRepository orderByOrderIdRepository;

    @InjectMocks
    private OrderIdService orderIdService;

    private static final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testGetOrderByOrderId() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        OrderId orderId = easyRandom.nextObject(OrderId.class);

        when(orderByOrderIdRepository.findOrderIdByOrderIdPrimaryKeyOrderId(any())).thenReturn(Mono.just(orderId));

        StepVerifier.create(orderIdService.getOrderByOrderId(UUID.randomUUID().toString()))
                .expectNext(orderId)
                .expectComplete()
                .verify();
    }

    @Test
    public void testSaveOrderId() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        OrderId orderId = easyRandom.nextObject(OrderId.class);

        when(orderByOrderIdRepository.save(any())).thenReturn(Mono.just(orderId));

        StepVerifier.create(orderIdService.saveOrderId(orderId))
                .expectComplete()
                .verify();

        verify(orderByOrderIdRepository).save(orderId);
    }

}
