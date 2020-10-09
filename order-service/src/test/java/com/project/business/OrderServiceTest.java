package com.project.business;

import com.project.client.PaymentServiceClient;
import com.project.model.Order;
import com.project.model.PaymentResponse;
import com.project.producer.OrderProducer;
import com.project.repository.OrderRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.project.utils.PaymentStatusCode.CONFIRMED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrdersCreator ordersCreator;

    @Mock
    private PaymentServiceClient paymentServiceClient;

    @Mock
    private OrderProducer orderProducer;

    @InjectMocks
    private OrderService orderService;

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testCheckoutOrderAndSave(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(ordersCreator.saveOrders(any())).thenReturn(Mono.empty());
        when(orderProducer.sendOder(any())).thenReturn(Mono.empty());

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setStatus(CONFIRMED.getValue());

        when(paymentServiceClient.payOrder(order)).thenReturn(Mono.just(paymentResponse));

        StepVerifier.create(orderService.checkoutOrderAndSave(order))
                .expectNext(paymentResponse)
                .expectComplete()
                .verify();

        verify(ordersCreator).saveOrders(orderCaptor.capture());
        verify(orderProducer).sendOder(any());

        assertThat(orderCaptor.getValue().getPaymentStatus()).isEqualTo("CONFIRMED");

    }

    @Test
    public void testSaveOrder(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(ordersCreator.saveOrders(any())).thenReturn(Mono.empty());
        when(orderProducer.sendOder(any())).thenReturn(Mono.empty());

        StepVerifier.create(orderService.saveOrder(order))
                .expectComplete()
                .verify();

        verify(ordersCreator).saveOrders(order);
        verify(orderProducer).sendOder(any());
    }

    @Test
    public void testGetOrderByUserEmail(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(orderRepository.findOrdersByOrderPrimaryKeyUserEmail(anyString())).thenReturn(Flux.just(order));

        StepVerifier.create(orderService.getOrderByUserEmail("toto@gmail.com"))
                .expectNextMatches(o -> o.getOrderPrimaryKey().getOrderId().toString().equals(order.getOrderPrimaryKey().getOrderId().toString()))
                .expectComplete()
                .verify();
    }


    @Test
    public void testAllOrders(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(orderRepository.findAll()).thenReturn(Flux.just(order));

        StepVerifier.create(orderService.getAllOrders())
                .expectNextMatches(o -> o.getOrderPrimaryKey().getUserEmail().equals(order.getOrderPrimaryKey().getUserEmail()))
                .expectComplete()
                .verify();
    }
}
