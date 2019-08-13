package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.business.PaymentService;
import com.project.configuration.ResourceServerConfigTest;
import com.project.model.Order;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@Import(ResourceServerConfigTest.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .ignoreRandomizationErrors(true)
            .collectionSizeRange(0, 2)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testPayOk() throws Exception {
        EasyRandom easyRandom = new EasyRandom();
        Order order = easyRandom.nextObject(Order.class);

        when(paymentService.checkout(any(Order.class))).thenReturn(1);

        MvcResult response = mockMvc.perform(post("/pay")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(response.getResponse().getContentAsString()).isEqualTo("1");
    }

    @Test
    public void testPayRefused() throws Exception {
        EasyRandom easyRandom = new EasyRandom();
        Order order = easyRandom.nextObject(Order.class);

        when(paymentService.checkout(any(Order.class))).thenReturn(0);

        MvcResult response = mockMvc.perform(post("/pay")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(response.getResponse().getContentAsString()).isEqualTo("2");
    }


}
