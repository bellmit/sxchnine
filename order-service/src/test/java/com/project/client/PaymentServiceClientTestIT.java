package com.project.client;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.config.LocalRibbonClientConfigurationTest;
import com.project.model.Order;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.SocketUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PaymentServiceClient.class,
        ObjectMapper.class, LocalRibbonClientConfigurationTest.class})
@ActiveProfiles("test")
@ImportAutoConfiguration({RibbonAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class})
@DirtiesContext
public class PaymentServiceClientTestIT {

    @Autowired
    private PaymentServiceClient paymentServiceClient;

    @Autowired
    private ObjectMapper objectMapper;

    private static int port = SocketUtils.findAvailableTcpPort();

    @Rule
    public PactProviderRuleMk2 pactProviderRuleMk2 = new PactProviderRuleMk2("payment-service", "localhost", port, this);

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0,2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);
    private EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
    private Order order = easyRandom.nextObject(Order.class);

    @BeforeClass
    public static void setup() {
        System.setProperty("port.ribbon", String.valueOf(port));
    }

    @Pact(provider = "payment-service", consumer = "order-service")
    public RequestResponsePact pactForPayment(PactDslWithProvider builder) throws JsonProcessingException {
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type", APPLICATION_JSON.toString());

        return builder.given("Pay Order")
                .uponReceiving("A POST request to /pay")
                .path("/pay")
                .method("POST")
                .headers(requestHeader)
                .body(objectMapper.writeValueAsString(order), APPLICATION_JSON)
                .willRespondWith()
                .status(200)
                .body("1")
                .headers(requestHeader)
                .toPact();
    }

    @PactVerification(fragment = "pactForPayment")
    @Test
    public void testPayOrder() {
        int paymentStatus = paymentServiceClient.payOrder(order).block();

        assertThat(paymentStatus).isEqualTo(1);
    }

    public static int findRandomPort(){
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            return serverSocket.getLocalPort();

        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }

}
