package com.project.business;

import com.project.model.Product;
import com.project.repository.ProductRepository;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.TestObjectCreator;
import utils.Unit;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Category(Unit.class)
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

        when(productRepository.findById("1")).thenReturn(Optional.of(TestObjectCreator.createProduct()));

        Product productFound = productService.getProductById("1");

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(productRepository).findById(argumentCaptor.capture());

        assertEquals("1", argumentCaptor.getValue());

        assertEquals("p1", productFound.getName());
        assertEquals(BigDecimal.valueOf(1.0), productFound.getPrice());
    }

    @Test
    public void testGetAllProducts(){
        when(productRepository.findAll()).thenReturn(Collections.singletonList(TestObjectCreator.createProduct()));

        List<Product> productList = productService.getAllProducts();

        assertEquals("1", productList.get(0).getId());
        assertEquals("p1", productList.get(0).getName());
        assertEquals(BigDecimal.valueOf(1.0), productList.get(0).getPrice());
    }

    @Test
    public void testSave(){
        Product product = TestObjectCreator.createProduct();
        when(productRepository.save(product)).thenReturn(product);

        Product productSaved = productService.save(product);

        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(argumentCaptor.capture());

        assertEquals("1", argumentCaptor.getValue().getId());

        assertEquals("1", productSaved.getId());
        assertEquals("p1", productSaved.getName());
        assertEquals(BigDecimal.valueOf(1.0), productSaved.getPrice());

        verify(kafkaProducer).sendProduct(product);

    }

    @Test
    public void testDeleteProductById(){
        productService.deleteProductById("1");
    }

}
