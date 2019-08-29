package com.project.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.business.ProductService;
import com.project.config.ResourceServerConfigTest;
import com.project.model.Product;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@Import(ResourceServerConfigTest.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testSearchAllProduct() throws Exception {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Product product = easyRandom.nextObject(Product.class);

        when(productService.getAllProducts()).thenReturn(Collections.singletonList(product));

        MvcResult result = mockMvc.perform(get("/search/all"))
                .andExpect(status().isOk())
                .andReturn();

        List<Product> savedProducts = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<Product>>() {});

        assertThat(savedProducts).contains(product);

    }

    @Test
    public void testSearchProduct() throws Exception {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Product product = easyRandom.nextObject(Product.class);

        when(productService.getProductsByQuery(anyString())).thenReturn(Collections.singletonList(product));

        MvcResult result = mockMvc.perform(get("/search/nike"))
                .andExpect(status().isOk())
                .andReturn();

        List<Product> savedProducts = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<Product>>(){});

        assertThat(savedProducts).contains(product);
    }

    @Test
    public void testAdvancedSearchProduct() throws Exception {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Product product = easyRandom.nextObject(Product.class);

        when(productService.getProductsByAdvancedFiltering(any(), any(), any())).thenReturn(Collections.singletonList(product));

        MvcResult result = mockMvc.perform(get("/advancedSearch?brand=nike&category=tshirt&size=M"))
                .andExpect(status().isOk())
                .andReturn();

        List<Product> savedProducts = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<Product>>(){});

        assertThat(savedProducts).contains(product);
    }

    @Test
    public void testSave() throws Exception {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Product product = easyRandom.nextObject(Product.class);

        doNothing().when(productService).save(any(Product.class));

        mockMvc.perform(post("/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk());

        verify(productService).save(product);
    }

    @Test
    public void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteById(anyString());

        mockMvc.perform(delete("/delete/1"))
                .andExpect(status().isOk());

        verify(productService).deleteById("1");
    }

    @Test
    public void testSearch404() throws Exception {
        mockMvc.perform(get("/search/product/1")).andExpect(status().isNotFound());
    }

    @Test
    public void testSaveBadRequest() throws Exception {
        mockMvc.perform(post("/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }
}
