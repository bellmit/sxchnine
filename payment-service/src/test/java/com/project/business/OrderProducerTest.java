package com.project.business;

import com.project.model.Order;
import org.jeasy.random.EasyRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderProducerTest {

    @Mock
    private KafkaTemplate kafkaTemplate;

    @InjectMocks
    private OrderProducer orderProducer;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(orderProducer, "topic", "topic");
    }

    @Test
    public void testSendOrder(){
        EasyRandom easyRandom = new EasyRandom();
        Order order = easyRandom.nextObject(Order.class);

        when(kafkaTemplate.send(anyString(), any(Order.class))).thenReturn(null);

        orderProducer.sendOrder(order);

        verify(kafkaTemplate).send("topic", order);
    }

    @Test(expected = NullPointerException.class)
    public void testSendOrderException(){
        orderProducer.sendOrder(new Order());
    }
}
