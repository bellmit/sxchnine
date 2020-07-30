package com.project.business;

import com.project.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(topics = "products")
@TestPropertySource(properties = {"spring.autoconfigure.exclude=" +
        "org.springframework.cloud.stream.test.binder.TestSupportBinderAutoConfiguration"})
@DirtiesContext
@Testcontainers
@Slf4j
public class ProductServiceTestIT {

    @Autowired
    private ProductService productService;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, "orders");

    @Test
    public void testGetProductsByQuery(){
        Product product = new Product();
        product.setId("1");
        product.setName("classic bob nike");
        product.setCategory("t-shirt");
        product.setBrand("nike");

        Product searchedProduct = productService.save(product)
                .thenMany(productService.getProductsByQuery("nike")).blockFirst();

        assertThat(searchedProduct.getName()).isEqualTo(product.getName());

    }

    @Test
    public void testGetProductsByAdvancedFilteringSearchBrand(){
        Product product = new Product();
        product.setId("2");
        product.setName("retro adidas");
        product.setCategory("t-shirt");
        product.setBrand("carhartt");

        Flux<Product> searchedProduct = productService.save(product)
                .thenMany(productService.getProductsByAdvancedFiltering("", "carhartt", "", ""));

        StepVerifier.create(searchedProduct)
                .expectNextMatches(p -> p.getName().equals(product.getName()))
                .expectComplete()
                .verify();
    }


    @Test
    public void testGetProductsByAdvancedFilteringSearchCategory(){
        Product product = new Product();
        product.setId("3");
        product.setName("classic bob nike");
        product.setCategory("jacket");
        product.setBrand("nike");

        Flux<Product> searchedProduct = productService.save(product)
                .thenMany(productService.getProductsByAdvancedFiltering("", "", "jacket", ""));

        StepVerifier.create(searchedProduct)
                .expectNextMatches(p -> p.getName().equals(product.getName()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetProductsByAdvancedFilteringSearchSize(){
        Product product = new Product();
        product.setId("4");
        product.setName("classic bob nike");
        product.setCategory("t-shirt");
        product.setBrand("nike");
        product.setSize(Collections.singletonList("M"));

        Product product2 = new Product();
        product2.setId("5");
        product2.setName("retro adidas");
        product2.setCategory("t-shirt");
        product2.setBrand("adidas");
        product2.setSize(Collections.singletonList("L"));

        productService.save(product).subscribe();
        Flux<Product> searchedProducts = productService.save(product2)
                .thenMany(productService.getProductsByAdvancedFiltering("", "", "", "L"));

        StepVerifier.create(searchedProducts)
                .expectNextMatches(p -> p.getName().equals(product2.getName()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetProductsByAdvancedFiltering(){
        Product product = new Product();
        product.setId("6");
        product.setName("classic bob nike");
        product.setCategory("t-shirt");
        product.setBrand("reebok");
        product.setSize(Collections.singletonList("M"));

        Flux<Product> productFlux = productService.save(product)
                .thenMany(productService.getProductsByAdvancedFiltering("", "reebok", "t-shirt", "M"));

        StepVerifier.create(productFlux)
                .expectNextMatches(p -> p.getName().equals(product.getName()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetProductsByAdvancedFilteringNotFound(){
        Product product = new Product();
        product.setName("classic bob nike");
        product.setCategory("t-shirt");
        product.setBrand("nike");
        product.setSize(Collections.singletonList("M"));

        Flux<Product> productFlux = productService.save(product)
                .thenMany(productService.getProductsByAdvancedFiltering("", "nike", "cap", "M"));

        StepVerifier.create(productFlux)
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }


    @Test
    public void testDeleteById(){
        Product product = new Product();
        product.setId("10");
        product.setName("classic bob nike");
        product.setCategory("t-shirt");
        product.setBrand("nike");
        product.setSize(Collections.singletonList("M"));

        Mono<Void> voidMono = productService.save(product)
                .thenMany(productService.getProductsByAdvancedFiltering("", "nike", "cap", "M"))
                .then(productService.deleteById("10"));

        StepVerifier.create(voidMono)
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }
}
