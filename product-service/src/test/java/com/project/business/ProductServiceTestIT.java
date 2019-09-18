package com.project.business;

import com.project.ProductServiceApplication;
import com.project.exception.ProductNotFoundException;
import com.project.model.Product;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import utils.TestObjectCreator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = ProductServiceApplication.class)
@TestPropertySource(properties = {"application-intg.yml"})
@ActiveProfiles("intg")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EmbeddedKafka
@DirtiesContext
public class ProductServiceTestIT {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProductService productService;

    @MockBean
    private KafkaProducer kafkaProducer;

    @BeforeAll
    public void setup(){
        mongoTemplate.createCollection("products");
        mongoTemplate.save(TestObjectCreator.createProduct());
    }

    @AfterAll
    public void tearDown(){
        mongoTemplate.dropCollection("products");
    }

    @Test
    public void testGetProductById(){
        Product productById = productService.getProductById("1");
        assertEquals("1", productById.getId());
        assertEquals("p1", productById.getName());
    }

    @Test
    public void testGetProductByName(){
        Product product = productService.getProductByName("p1");
        assertEquals("1", product.getId());
        assertEquals("p1", product.getName());
    }

    @Test
    public void testGetAllProducts(){
        List<Product> allProducts = productService.getAllProducts();
        assertEquals(2, allProducts.size());
    }

    @Test
    public void testSaveProduct(){
        Product productToSave = new Product();
        productToSave.setId("2");
        productToSave.setName("p2");
        productToSave.setStore("s2");

        Product savedProduct = productService.save(productToSave);

        assertEquals("2", savedProduct.getId());
        assertEquals("p2", savedProduct.getName());
        assertEquals("s2", savedProduct.getStore());

        verify(kafkaProducer).sendProduct(savedProduct);
    }

    @Test
    public void testDeleteProductById(){
        Product productToDelete = new Product();
        productToDelete.setId("3");
        productToDelete.setName("p3");
        productToDelete.setStore("s3");

        Product savedProduct = productService.save(productToDelete);

        assertEquals("3", savedProduct.getId());
        assertEquals("p3", savedProduct.getName());
        assertEquals("s3", savedProduct.getStore());

        productService.deleteProductById("3");

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById("3"));
    }
}
