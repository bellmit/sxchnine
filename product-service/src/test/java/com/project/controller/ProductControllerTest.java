package com.project.controller;

import com.project.business.ProductService;
import com.project.model.Product;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import utils.TestObjectCreator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ProductController.class)
@DirtiesContext
public class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService productService;

    @Test
    public void testGetProductById(){
        when(productService.getProductById(anyLong())).thenReturn(Mono.just(new Product()));

        webTestClient.get()
                .uri("/id/1")
                .accept(APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Product.class);

        verify(productService).getProductById(1L);
    }

    @Test
    public void testGetProducts(){
        Product product = TestObjectCreator.createProduct();

        when(productService.getAllProducts()).thenReturn(Flux.just(product));

        webTestClient.get()
                .uri("/all?pageNo=0&pageSize=1")
                .accept(APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Product.class)
                .value(Product::getId, Matchers.equalTo(product.getId()))
                .value(Product::getName, Matchers.equalTo(product.getName()));

        verify(productService).getAllProducts();
    }

    @Test
    public void testCreateOrUpdateProduct() {

        webTestClient.post()
                .uri("/save")
                .accept(APPLICATION_STREAM_JSON)
                .body(Mono.just(TestObjectCreator.createProduct()), Product.class)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Product.class);

        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productService).save(argumentCaptor.capture());

        assertEquals(1, argumentCaptor.getValue().getId());
        assertEquals("p1", argumentCaptor.getValue().getName());
    }

    @Test
    public void testDeleteProductById(){
        webTestClient.delete()
                .uri("/delete/id/1")
                .exchange()
                .expectStatus().is2xxSuccessful();

        verify(productService).deleteProductById(1);
    }


}
