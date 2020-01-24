package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.business.ProductService;
import com.project.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import utils.ResourceServerConfigMock;
import utils.TestObjectCreator;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
@Import(ResourceServerConfigMock.class)
@DirtiesContext
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetProductById() throws Exception {
        when(productService.getProductById(anyString())).thenReturn(new Product());

        mockMvc.perform(MockMvcRequestBuilders.get("/id/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetProducts() throws Exception {
        when(productService.getAllProducts(anyInt(), anyInt())).thenReturn(Collections.singletonList(TestObjectCreator.createProduct()));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/all?pageNo=0&pageSize=1"))
                .andReturn().getResponse();

        assertTrue(response.getContentAsString().contains("1"));
        assertTrue(response.getContentAsString().contains("p1"));
        assertTrue(response.getContentAsString().contains("1.0"));
    }

    @Test
    public void testCreateOrUpdateProduct() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjectCreator.createProduct())))
                .andReturn().getResponse();

        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productService).save(argumentCaptor.capture());

        assertEquals("1", argumentCaptor.getValue().getId());
        assertEquals("p1", argumentCaptor.getValue().getName());

        assertEquals(200, response.getStatus());
    }

    @Test
    public void testDeleteProductById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/delete/id/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
