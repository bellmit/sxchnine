package com.project.business;

import com.project.model.Order;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderProducerTest {

    @Mock
    private KafkaSender kafkaSender;

    @InjectMocks
    private OrderProducer orderProducer;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(orderProducer, "topic", "topic");
    }

    @Test
    public void testSendOrder() {
        EasyRandom easyRandom = new EasyRandom();
        Order order = easyRandom.nextObject(Order.class);

        when(kafkaSender.send(any())).thenReturn(Flux.empty());

        orderProducer.sendOrder(order);

        verify(kafkaSender).send(any());
    }

    @Test
    public void testSendOrderException() {

        assertThrows(NullPointerException.class, () -> orderProducer.sendOrder(new Order()));
    }
}
