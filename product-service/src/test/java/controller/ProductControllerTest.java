package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.business.ProductService;
import com.project.controller.ProductController;
import com.project.model.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utils.TestObjectCreator;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private JacksonTester<Product> jsonMapper;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void testGetProductById() throws Exception {
        when(productService.getProductById(anyString())).thenReturn(new Product());

        mockMvc.perform(MockMvcRequestBuilders.get("/id/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.singletonList(TestObjectCreator.createProduct()));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/all"))
                .andReturn().getResponse();

        assertTrue(response.getContentAsString().contains("1"));
        assertTrue(response.getContentAsString().contains("p1"));
        assertTrue(response.getContentAsString().contains("1.0"));
    }

    @Test
    public void testCreateOrUpdateProduct() throws Exception {
        Product product = TestObjectCreator.createProduct();

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.write(TestObjectCreator.createProduct()).getJson()))
                .andReturn().getResponse();

        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productService).save(argumentCaptor.capture());

        assertEquals("1", argumentCaptor.getValue().getId());
        assertEquals("p1", argumentCaptor.getValue().getName());

        assertEquals(200, response.getStatus());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.write(TestObjectCreator.createProduct()).getJson()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteProductById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/delete/id/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
