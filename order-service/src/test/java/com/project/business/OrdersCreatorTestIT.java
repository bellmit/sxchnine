package com.project.business;

import com.project.config.CassandraTestConfig;
import com.project.model.Order;
import com.project.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.cassandraunit.spring.CassandraDataSet;
import org.cassandraunit.spring.CassandraUnitDependencyInjectionTestExecutionListener;
import org.cassandraunit.spring.CassandraUnitTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import reactor.core.CoreSubscriber;
import reactor.util.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"server.ssl.enabled=false", "spring.autoconfigure.exclude=" +
        "org.springframework.cloud.stream.test.binder.TestSupportBinderAutoConfiguration"})
@TestExecutionListeners(listeners = {
        CassandraUnitDependencyInjectionTestExecutionListener.class,
        CassandraUnitTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class
})
@EmbeddedCassandra(timeout = 300000L)
@CassandraDataSet(value = {"schema.cql"}, keyspace = "test2")
@EmbeddedKafka
@Import(CassandraTestConfig.class)
@DirtiesContext
@Slf4j
public class OrdersCreatorTestIT {

    @Autowired
    private OrdersCreator ordersCreator;

    @Autowired
    private OrderRepository orderRepository;

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .ignoreRandomizationErrors(true)
            .collectionSizeRange(1,2)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testSaveOrders() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSS");
        String format = LocalDateTime.now().format(formatter);

        order.setPaymentTime(LocalDateTime.parse(format));
        order.getOrderKey().setOrderTime(LocalDateTime.parse(format));
        order.setShippingTime(LocalDateTime.parse(format));

        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(context.hasKey(any())).thenReturn(false);
        when(tracer.nextSpan()).thenReturn(span);

        ordersCreator.saveOrders(order)
                .thenMany(orderRepository.findOrdersByOrderKeyUserEmail(order.getOrderKey().getUserEmail()))
                //.contextWrite(ctx -> context)
                .subscribe(new CoreSubscriber<Order>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        log.info("start subscribing");
                    }

                    @Override
                    public void onNext(Order order) {
                        log.info("start assertion");
                        assertThat(order).usingRecursiveComparison().ignoringFields("paymentInfo.type", "paymentInfo.paymentIntentId").isEqualTo(order);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public Context currentContext() {
                        return context;
                    }
                });

        //assertThat(first).usingRecursiveComparison().ignoringFields("paymentInfo.type", "paymentInfo.paymentIntentId").isEqualTo(order);
    }

}
