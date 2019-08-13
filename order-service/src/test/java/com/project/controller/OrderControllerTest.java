package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.business.OrderIdService;
import com.project.business.OrderService;
import com.project.config.ResourceServerConfigTest;
import com.project.model.Order;
import com.project.model.OrderId;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@Import(ResourceServerConfigTest.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderIdService orderIdService;

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testGetAllOrders() throws Exception {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(orderService.getAllOrders()).thenReturn(Collections.singletonList(order));

        MockHttpServletResponse response = mockMvc.perform(get("/all").
                contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getContentAsString()).contains(order.getOrderPrimaryKey().getOrderId().toString());
        assertThat(response.getContentAsString()).contains(order.getOrderPrimaryKey().getUserEmail());
    }

    @Test
    public void testGetOrdersByOrderId() throws Exception {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        OrderId orderId = easyRandom.nextObject(OrderId.class);

        when(orderIdService.getOrderByOrderId(anyString())).thenReturn(orderId);

        MockHttpServletResponse response = mockMvc.perform(get("/orderId/1").
                contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(response.getContentAsString()).contains(orderId.getOrderIdPrimaryKey().getOrderId().toString());
        assertThat(response.getContentAsString()).contains(orderId.getOrderIdPrimaryKey().getUserEmail());
    }

    @Test
    public void testGetOrdersByEmail() throws Exception {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(orderService.getOrderByUserEmail(anyString())).thenReturn(Collections.singletonList(order));

        MockHttpServletResponse response = mockMvc.perform(get("/userEmail/toto@gmail.com").
                contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(response.getContentAsString()).contains(order.getOrderPrimaryKey().getOrderId().toString());
        assertThat(response.getContentAsString()).contains(order.getOrderPrimaryKey().getUserEmail());
    }


    @Test
    public void testCheckoutOrderAndSave() throws Exception {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        when(orderService.checkoutOrderAndSave(any(Order.class))).thenReturn(1);

        MockHttpServletResponse response = mockMvc.perform(post("/checkoutOrder").
                contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(response.getContentAsString()).contains("1");
    }


    @Test
    public void testSave() throws Exception {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        mockMvc.perform(post("/save").
                contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk());

    }

    @Test
    public void testNotFound() throws Exception {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Order order = easyRandom.nextObject(Order.class);

        mockMvc.perform(post("/saved").
                contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isNotFound());

    }


    @Test
    public void testBadRequest() throws Exception {
        mockMvc.perform(post("/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());

    }
}
