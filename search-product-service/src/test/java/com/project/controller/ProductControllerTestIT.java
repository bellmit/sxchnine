package com.project.controller;

import com.project.business.ProductService;
import com.project.model.Product;
import org.jeasy.random.EasyRandom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@EmbeddedKafka(topics = "products")
@ActiveProfiles("test")
@Testcontainers
@DirtiesContext
public class ProductControllerTestIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReactiveElasticsearchOperations elasticsearchOperations;


    @Test
    public void testSearchProduct(){
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);
        product.setBrand("brandNewTest");

        productService.save(product).block();

        webTestClient.get()
                .uri("/search/brandNewTest")
                .accept(APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Product.class)
                .value(Product::getBrand, equalTo(product.getBrand()));

    }

    @Test
    public void testAdvancedSearch(){
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);
        product.setBrand("toto");
        product.setCategory("titi");
        product.setSize(Collections.singletonList("l"));

        productService.save(product).block();

        webTestClient.get()
                .uri("/advancedSearch?brand=toto&category=titi&size=l")
                .accept(APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Product.class)
                .value(Product::getBrand, equalTo(product.getBrand()));
    }

    @Test
    public void testSave() {
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);

        Flux<Product> productFlux = Mono.fromRunnable(() -> {
            webTestClient.post().uri("/save")
                    .body(Mono.just(product), Product.class)
                    .exchange()
                    .expectStatus().is2xxSuccessful();
        }).thenMany(productService.getProductsByQuery(product.getName()));

        StepVerifier.create(productFlux)
                .expectNextMatches(p -> p.getName().equals(product.getName()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testDeleteById(){
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);
        product.setId("100");
        product.setBrand("100");

        productService.save(product).subscribe();

        Flux<Product> productFlux = Mono.fromRunnable(() -> {
            webTestClient.delete()
                    .uri("/delete/100")
                    .exchange()
                    .expectStatus().is2xxSuccessful();
        }).thenMany(productService.getProductsByQuery(product.getBrand()));

        StepVerifier.create(productFlux)
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }
}
