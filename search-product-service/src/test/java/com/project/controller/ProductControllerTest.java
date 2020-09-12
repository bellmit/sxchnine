package com.project.controller;

import com.project.business.ProductService;
import com.project.model.Product;
import org.hamcrest.Matchers;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;

@WebFluxTest(controllers = ProductController.class)
@ActiveProfiles("dev")
public class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService productService;

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testSearchProduct() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Product product = easyRandom.nextObject(Product.class);

        when(productService.getProductsByQuery(anyString())).thenReturn(Flux.just(product));

        webTestClient.get()
                .uri("/search/nike")
                .accept(APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Product.class)
                .value(Product::getId, Matchers.equalTo(product.getId()))
                .value(Product::getName, Matchers.equalTo(product.getName()));

        verify(productService).getProductsByQuery(anyString());
    }

    @Test
    public void testAdvancedSearchProduct() throws Exception {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Product product = easyRandom.nextObject(Product.class);

        when(productService.getProductsByAdvancedFiltering(any(), any(), any(), any())).thenReturn(Flux.just(product));

        webTestClient.get()
                .uri("/advancedSearch?brand=nike&category=tshirt&size=M")
                .accept(APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Product.class)
                .value(Product::getId, Matchers.equalTo(product.getId()))
                .value(Product::getName, Matchers.equalTo(product.getName()));

        verify(productService).getProductsByAdvancedFiltering(null, "nike", "tshirt", "M");
    }

    @Test
    public void testSave() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        Product product = easyRandom.nextObject(Product.class);

        when(productService.save(any(Product.class))).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/save")
                .body(Mono.just(product), Product.class)
                .exchange()
                .expectStatus().is2xxSuccessful();

        verify(productService).save(product);
    }

    @Test
    public void testDeleteProduct() throws Exception {
        when(productService.deleteById(anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/delete/1")
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        verify(productService).deleteById("1");
    }

    @Test
    public void testSearch404() {
        webTestClient.get()
                .uri("/search/product/1")
                .exchange()
                .expectStatus().isNotFound();
    }
}
