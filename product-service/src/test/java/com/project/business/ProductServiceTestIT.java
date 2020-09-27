package com.project.business;

import com.project.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import utils.TestObjectCreator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(PER_CLASS)
@DirtiesContext
public class ProductServiceTestIT {

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Autowired
    private ProductService productService;

    @MockBean
    private KafkaProducer kafkaProducer;

    @AfterEach
    public void dropCollection(){
        mongoTemplate.dropCollection("products").block();
    }

    @Test
    public void testGetProductById(){
        Mono<Product> products = mongoTemplate.createCollection("products")
                .then(mongoTemplate.save(TestObjectCreator.createProduct()))
                .then(productService.getProductById(1L));

        StepVerifier.create(products)
                .expectSubscription()
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetProductByName(){
        Mono<Product> productMono = mongoTemplate.createCollection("products")
                .then(mongoTemplate.save(TestObjectCreator.createProduct()))
                .then(productService.getProductByName("p1"));

        StepVerifier.create(productMono)
                .expectSubscription()
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetAllProducts(){
        Flux<Product> productFlux = mongoTemplate.save(TestObjectCreator.createProduct())
                .thenMany(productService.getAllProducts());

        StepVerifier.create(productFlux)
                .expectSubscription()
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    @Test
    public void testSaveProduct() {
        when(kafkaProducer.sendProduct(any())).thenReturn(Mono.just(TestObjectCreator.createProduct()));
        Product product = productService.save(TestObjectCreator.createProduct())
                .flatMap(p -> productService.getProductById(1L)).block();

        assertEquals(1, product.getId());
        assertEquals("p1", product.getName());
        assertEquals(1.0, product.getPrice().doubleValue());
        verify(kafkaProducer).sendProduct(any());

    }

    @Test
    public void testDeleteProductById(){
        Mono<Product> product = mongoTemplate.save(TestObjectCreator.createProduct())
                .flatMap(p -> productService.getProductByName("p1"))
                .flatMap(p -> productService.deleteProductById(1))
                .then(productService.getProductById(1L));

        StepVerifier.create(product)
                .expectSubscription()
                .expectNextCount(0)
                .expectComplete()
                .verify();

    }
}
