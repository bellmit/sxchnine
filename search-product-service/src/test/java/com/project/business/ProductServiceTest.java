package com.project.business;

import com.project.model.Product;
import org.jeasy.random.EasyRandom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private ReactiveElasticsearchOperations reactiveElasticsearchOperations;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testGetProductsByQuery(){
        when(reactiveElasticsearchOperations.search(any(), eq(Product.class), eq(Product.class))).thenReturn(Flux.empty());

        productService.getProductsByQuery("test");

        verify(reactiveElasticsearchOperations).search(any(), eq(Product.class), eq(Product.class));
    }

    @Test
    public void testGetProductsByAdvancedFiltering(){
        when(reactiveElasticsearchOperations.search(any(), eq(Product.class), eq(Product.class))).thenReturn(Flux.empty());

        productService.getProductsByAdvancedFiltering("", "brand", "category", "size");

        verify(reactiveElasticsearchOperations).search(any(), eq(Product.class), eq(Product.class));
    }

    @Test
    public void testSave() {
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);

        when(reactiveElasticsearchOperations.save(any(Product.class))).thenReturn(Mono.empty());

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        productService.save(product);

        verify(reactiveElasticsearchOperations).save(productCaptor.capture());

        assertThat(productCaptor.getValue()).usingRecursiveComparison().isEqualTo(product);
    }

    @Test
    public void testDeleteById(){
        when(reactiveElasticsearchOperations.delete(anyString(), eq(Product.class))).thenReturn(Mono.empty());

        productService.deleteById("id");

        verify(reactiveElasticsearchOperations).delete(eq("id"), eq(Product.class));
    }

}
