package com.project.business;

import com.project.model.Product;
import com.project.repository.ProductRepository;
import org.jeasy.random.EasyRandom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.support.Acknowledgment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductConsumerTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Acknowledgment acknowledgment;

    @InjectMocks
    private ProductConsumer productConsumer;

    @Test
    public void testConsumeProduct(){
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        productConsumer.consumeProduct(product, acknowledgment);

        verify(productRepository, timeout(1)).save(productCaptor.capture());

        assertThat(product).isEqualToComparingFieldByFieldRecursively(productCaptor.getValue());
    }
}
