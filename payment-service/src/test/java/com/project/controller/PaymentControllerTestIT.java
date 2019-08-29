package com.project.controller;

import com.project.configuration.ResourceServerConfigTest;
import com.project.model.Order;
import org.jeasy.random.EasyRandom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@EmbeddedKafka
@ActiveProfiles("test")
@Import(ResourceServerConfigTest.class)
public class PaymentControllerTestIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testPayOrder(){
        EasyRandom easyRandom = new EasyRandom();
        Order order = easyRandom.nextObject(Order.class);

        ResponseEntity<Object> response = testRestTemplate.postForEntity("/pay", order, Object.class);

        assertThat(response.getBody()).isEqualTo(0);
    }
}
