package com.project.business;

import com.project.model.Product;
import com.project.repository.ProductRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.jeasy.random.EasyRandom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testGetProductsByQuery(){
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);

        when(productRepository.search(any(QueryBuilder.class))).thenReturn(Collections.singletonList(product));

        Iterable<Product> products = productService.getProductsByQuery("test");

        assertThat(products).contains(product);
    }

    @Test
    public void testGetProductsByAdvancedFiltering(){
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);

        when(productRepository.search(any(QueryBuilder.class))).thenReturn(Collections.singletonList(product));

        Iterable<Product> products = productService.getProductsByAdvancedFiltering("brand", "category", "size");

        assertThat(products).contains(product);
    }

    @Test
    public void testGetAllProducts(){
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);

        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        Iterable<Product> allProducts = productService.getAllProducts();

        assertThat(allProducts).contains(product);
    }

    @Test
    public void testSave() {
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        productService.save(product);

        verify(productRepository).save(productCaptor.capture());

        assertThat(productCaptor.getValue()).isEqualToComparingFieldByFieldRecursively(product);
    }

    @Test
    public void testSaveProducts() {
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);

        ArgumentCaptor<List> productCaptor = ArgumentCaptor.forClass(List.class);

        when(productRepository.saveAll(anyList())).thenReturn(Collections.singletonList(product));

        productService.saveProducts(Collections.singletonList(product));

        verify(productRepository).saveAll(productCaptor.capture());

        assertThat(productCaptor.getValue()).contains(product);

    }

    @Test
    public void testDelete(){
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        doNothing().when(productRepository).delete(any(Product.class));

        productService.delete(product);

        verify(productRepository).delete(productCaptor.capture());

        assertThat(productCaptor.getValue()).isEqualToComparingFieldByFieldRecursively(product);
    }

    @Test
    public void testDeleteById(){
        doNothing().when(productRepository).deleteById(anyString());

        productService.deleteById("1");

        verify(productRepository).deleteById("1");
    }

    @Test
    public void testDeleteProducts(){
        doNothing().when(productRepository).deleteAll(anyList());

        productService.deleteProducts(new ArrayList<>());

        verify(productRepository).deleteAll(new ArrayList<>());
    }

}
