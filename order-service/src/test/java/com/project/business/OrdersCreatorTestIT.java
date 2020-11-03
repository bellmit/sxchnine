package com.project.business;

import com.project.config.CassandraTestConfig;
import com.project.model.Order;
import com.project.repository.OrderRepository;
import org.cassandraunit.spring.CassandraDataSet;
import org.cassandraunit.spring.CassandraUnitDependencyInjectionTestExecutionListener;
import org.cassandraunit.spring.CassandraUnitTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
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
    public void testSaveOrders() throws InterruptedException {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSS");
        String format = LocalDateTime.now().format(formatter);

        order.setPaymentTime(LocalDateTime.parse(format));
        order.getOrderKey().setOrderTime(LocalDateTime.parse(format));
        order.setShippingTime(LocalDateTime.parse(format));

        Order first = ordersCreator.saveOrders(order)
                .thenMany(orderRepository.findOrdersByOrderKeyUserEmail(order.getOrderKey().getUserEmail()))
                .blockFirst();

        assertThat(first).usingRecursiveComparison().ignoringFields("paymentInfo.type", "paymentInfo.paymentIntentId").isEqualTo(order);
    }

}
