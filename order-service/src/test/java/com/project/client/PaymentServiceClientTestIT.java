package com.project.client;


import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.Order;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "payment-service", port = "9000")
@SpringBootTest(classes = {PaymentServiceClient.class,
        ObjectMapper.class, WebClientConfig.class, WebClient.class, RefreshAutoConfiguration.class})
@ActiveProfiles("test")
@DirtiesContext
public class PaymentServiceClientTestIT {

    @Autowired
    private ObjectMapper objectMapper;

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0,2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);
    private final EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
    private final Order order = easyRandom.nextObject(Order.class);


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

    @Test
    @PactTestFor(pactMethod = "pactForPayment")
    public void testPayOrder(MockServer mockServer) throws IOException {
        HttpResponse httpResponse = Request.Post(mockServer.getUrl() + "/pay")
                .bodyString(objectMapper.writeValueAsString(order), APPLICATION_JSON)
                .execute()
                .returnResponse();

        assertThat(IOUtils.toString(httpResponse.getEntity().getContent())).contains("1");
    }

}
