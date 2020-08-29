package com.project.business;

import com.project.config.CassandraTestConfig;
import com.project.model.OrderId;
import org.cassandraunit.spring.CassandraDataSet;
import org.cassandraunit.spring.CassandraUnitDependencyInjectionTestExecutionListener;
import org.cassandraunit.spring.CassandraUnitTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"server.ssl.enabled=false", "spring.autoconfigure.exclude=" +
        "org.springframework.cloud.stream.test.binder.TestSupportBinderAutoConfiguration"})
@TestExecutionListeners(listeners = {
        CassandraUnitDependencyInjectionTestExecutionListener.class,
        CassandraUnitTestExecutionListener.class,
        ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class
})
@EmbeddedCassandra(timeout = 300000L)
@CassandraDataSet(value = {"schema.cql"}, keyspace = "test2")
@EmbeddedKafka
@Import(CassandraTestConfig.class)
@DirtiesContext
public class OrderIdServiceTestIT {

    @Autowired
    private OrderIdService orderIdService;

    private static final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, "products");

    @Test
    public void testSaveOrderId() throws InterruptedException {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        OrderId orderId = easyRandom.nextObject(OrderId.class);

        Thread.sleep(1000L);

        orderIdService.saveOrderId(orderId)
                .then(orderIdService.getOrderByOrderId(orderId.getOrderIdPrimaryKey().getOrderId().toString()))
                .subscribe(o -> assertThat(o).usingRecursiveComparison().isEqualTo(orderId));
    }
}
