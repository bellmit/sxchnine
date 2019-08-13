package com.project.business;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.project.configuration.FeignConfiguration;
import com.project.model.Order;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.SocketUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FeignConfiguration.class, PaymentService.class, OrderClient.class,
        OrderClientFallback.class, OrderProducer.class, KafkaAutoConfiguration.class})
@EmbeddedKafka
@ActiveProfiles("test")
public class PaymentServiceTestIT {

    @Autowired
    private PaymentService paymentService;

    private static int port = SocketUtils.findAvailableTcpPort();

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .scanClasspathForConcreteTypes(true)
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true);

    @ClassRule
    public static WireMockClassRule wireMockClassRule = new WireMockClassRule(port);

    @BeforeClass
    public static void setup(){
        System.setProperty("ribbon.port", String.valueOf(port));
    }

    @Test
    public void testRecheckout(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);
        order.setPaymentStatus("1");

        stubFor(post("/save").willReturn(aResponse().withStatus(200)));

        paymentService.recheckout(order);
    }
}
