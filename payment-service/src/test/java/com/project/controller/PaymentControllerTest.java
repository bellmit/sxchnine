/*
package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.business.PaymentService;
import com.project.model.Order;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
@WebFluxTest
public class PaymentControllerTest {

    @Autowired
    private Web mockMvc;

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
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
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
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(paymentService.checkout(any(Order.class))).thenReturn(0);

        MvcResult response = mockMvc.perform(post("/pay")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(response.getResponse().getContentAsString()).isEqualTo("0");
    }

    @Test
    public void testPayNotFound() throws Exception {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        MvcResult response = mockMvc.perform(post("/payed")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(404);
    }

    @Test
    public void testPayBadRequest() throws Exception {
        MvcResult response = mockMvc.perform(post("/pay")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("")))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(400);
    }


}
*/
