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
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        when(productRepository.findById("1")).thenReturn(Mono.just(TestObjectCreator.createProduct()));

        Mono<Product> productFound = productService.getProductById("1");

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(productRepository).findById(argumentCaptor.capture());

        assertEquals("1", argumentCaptor.getValue());

        assertEquals("p1", productFound.block().getName());
        assertEquals(BigDecimal.valueOf(1.0), productFound.block().getPrice());
    }

    @Test
    public void testGetAllProducts(){
        when(productRepository.findAll()).thenReturn(Flux.empty());

        productService.getAllProducts(0, 2);

        Pageable paging = PageRequest.of(0, 2, Sort.unsorted());

        verify(productRepository).findAll();
    }

    @Test
    public void testSave(){
        Product product = Mono.just(TestObjectCreator.createProduct()).block();

        when(productRepository.save(product)).thenReturn(Mono.just(product));

        Mono<Product> productSaved = productService.save(product);

        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(argumentCaptor.capture());

        assertEquals("1", argumentCaptor.getValue().getId());

        assertEquals("1", productSaved.block().getId());
        assertEquals("p1", productSaved.block().getName());
        assertEquals(BigDecimal.valueOf(1.0), productSaved.block().getPrice());

        verify(kafkaProducer).sendProduct(product);

    }

    @Test
    public void testDeleteProductById(){
        productService.deleteProductById("1");
    }

    @Test
    public void test() throws InterruptedException {
        CountDownLatch count = new CountDownLatch(1);
        Flux.generate(c -> c.next(m())).log().doOnNext(p -> p.toString());

        System.out.println(Thread.currentThread().getName() + " Welcome ");

        count.await();
    }

    private String m(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Thread.currentThread().getName() + " - toto";
    }
}
