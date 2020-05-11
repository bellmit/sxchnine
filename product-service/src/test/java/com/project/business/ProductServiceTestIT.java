package com.project.business;

import com.project.ProductServiceApplication;
import com.project.exception.ProductNotFoundException;
import com.project.model.Product;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import utils.TestObjectCreator;

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
        Mono<Product> productById = productService.getProductById("1");
        assertEquals("1", productById.block().getId());
        assertEquals("p1", productById.block().getName());
    }

    @Test
    public void testGetProductByName(){
        Mono<Product> product = productService.getProductByName("p1");
        assertEquals("1", product.block().getId());
        assertEquals("p1", product.block().getName());
    }

    @Test
    public void testGetAllProducts(){
        Flux<Product> allProducts = productService.getAllProducts(0, 2);
        assertEquals(2, allProducts.toStream().count());
    }

    @Test
    public void testSaveProduct(){
        Product productToSave = new Product();
        productToSave.setId("2");
        productToSave.setName("p2");
        productToSave.setStore("s2");

        Mono<Product> savedProduct = productService.save(productToSave);

        assertEquals("2", savedProduct.block().getId());
        assertEquals("p2", savedProduct.block().getName());
        assertEquals("s2", savedProduct.block().getStore());

        verify(kafkaProducer).sendProduct(savedProduct.block());
    }

    @Test
    public void testDeleteProductById(){
        Product productToDelete = new Product();
        productToDelete.setId("3");
        productToDelete.setName("p3");
        productToDelete.setStore("s3");

        Mono<Product> savedProduct = productService.save(productToDelete);

        assertEquals("3", savedProduct.block().getId());
        assertEquals("p3", savedProduct.block().getName());
        assertEquals("s3", savedProduct.block().getStore());

        productService.deleteProductById("3");

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById("3"));
    }
}
