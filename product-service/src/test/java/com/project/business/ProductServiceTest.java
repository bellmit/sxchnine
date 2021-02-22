package com.project.business;

import com.project.model.Product;
import com.project.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import utils.TestObjectCreator;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testGetProductById(){

        when(productRepository.findProductById(1L)).thenReturn(Mono.just(TestObjectCreator.createProduct()));

        Mono<Product> productFound = productService.getProductById(1L);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);

        verify(productRepository).findProductById(argumentCaptor.capture());

        assertEquals(1, argumentCaptor.getValue());

        assertEquals("p1", productFound.block().getName());
        assertEquals(BigDecimal.valueOf(1.0), productFound.block().getPrice());
    }

    @Test
    public void testGetAllProducts(){
        when(productRepository.findAll()).thenReturn(Flux.empty());

        productService.getAllProducts();

        Pageable paging = PageRequest.of(0, 2, Sort.unsorted());

        verify(productRepository).findAll();
    }

    @Test
    public void testSave(){
        Product product = TestObjectCreator.createProduct();

        when(productRepository.save(product)).thenReturn(Mono.just(product));
        when(kafkaProducer.sendProduct(any())).thenReturn(Mono.just(product));

        Product productSaved = productService.save(product).block();

        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(argumentCaptor.capture());

        assertEquals(1L, argumentCaptor.getValue().getId().longValue());

        assertEquals(1L, productSaved.getId().longValue());
        assertEquals("p1", productSaved.getName());
        assertEquals(BigDecimal.valueOf(1.0), productSaved.getPrice());

        verify(kafkaProducer).sendProduct(any());

    }

    @Test
    public void testDeleteProductById(){
        productService.deleteProductById(1);
    }

}
