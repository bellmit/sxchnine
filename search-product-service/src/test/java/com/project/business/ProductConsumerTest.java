package com.project.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.Product;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductConsumerTest {

    @Mock
    private ProductService productService;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private Acknowledgment acknowledgment;

    @InjectMocks
    private ProductConsumer productConsumer;

    @Test
    public void testConsumeProduct() throws JsonProcessingException {
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);

        when(productService.save(any(Product.class))).thenReturn(Mono.empty());

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        productConsumer.consumeProduct(objectMapper.writeValueAsString(product), acknowledgment);

        verify(productService).save(productCaptor.capture());

        assertThat(product).usingRecursiveComparison().isEqualTo(productCaptor.getValue());
    }
}
