package com.project.business;

import com.project.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import utils.TestObjectCreator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(properties = {"application-test.yml"})
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
@Slf4j
public class ProductServiceTestIT {

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Autowired
    private ProductService productService;

    @MockBean
    private KafkaProducer kafkaProducer;


    @Test
    public void testGetProductById(){
        mongoTemplate.createCollection("products")
                .flatMap(c -> mongoTemplate.save(TestObjectCreator.createProduct()))
                .flatMap(p -> productService.getProductById(1))
                .doOnNext(p -> {
                    assertEquals(1, p.getId());
                    assertEquals("p1", p.getName());
                })
                .then(mongoTemplate.dropCollection("products"))
                .subscribe();
    }

    @Test
    public void testGetProductByName(){
        mongoTemplate.createCollection("products")
                .flatMap(c -> mongoTemplate.save(TestObjectCreator.createProduct()))
                .flatMap(p -> productService.getProductByName("p1"))
                .doOnNext(p -> {
                    assertEquals(1, p.getId());
                    assertEquals("p1", p.getName());
                })
                .then(mongoTemplate.dropCollection("products"))
                .subscribe();
    }

    @Test
    public void testGetAllProducts(){
        mongoTemplate.save(TestObjectCreator.createProduct())
                .log()
                .map(p -> productService.getAllProducts())
                .flatMap(Flux::count)
                .doOnNext(p -> {
                    System.out.println("----- start assertions ----");
                    assertEquals(1, p);
                })
                .then(mongoTemplate.dropCollection("products"))
                .subscribe();
    }

    @Test
    public void testSaveProduct() throws InterruptedException {
        when(kafkaProducer.sendProduct(any())).thenReturn(Mono.just(TestObjectCreator.createProduct()));
        productService.save(TestObjectCreator.createProduct())
                .flatMap(p -> productService.getProductById(1))
                .doOnNext(p -> {
                    System.out.println("--- start assertion --- ");
                    assertEquals(1, p.getId());
                    assertEquals("p1", p.getName());
                    assertEquals(1.0, p.getPrice().doubleValue());
                    verify(kafkaProducer).sendProduct(any());
                })
                .then(mongoTemplate.dropCollection(Product.class))
                .subscribe();
    }

    @Test
    public void testDeleteProductById(){
        mongoTemplate.save(TestObjectCreator.createProduct())
                .flatMap(p -> productService.getProductByName("p1"))
                .doOnNext(p -> {
                    assertEquals(1, p.getId());
                    assertEquals("p1", p.getName());
                })
                .flatMap(p -> productService.deleteProductById(1))
                .flatMap(p -> mongoTemplate.findById(1, Product.class))
                .doOnNext(Assertions::assertNull)
                .flatMap(p -> productService.getProductById(1))
                .doOnNext(Assertions::assertNotNull)
                .then(mongoTemplate.dropCollection(Product.class))
                .subscribe();
    }
}
