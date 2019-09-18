package com.project.business;


import com.project.exception.ProductNotFoundException;
import com.project.model.Product;
import com.project.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class ProductService {

    private ProductRepository productRepository;

    private KafkaProducer kafkaProducer;

    public ProductService(ProductRepository productRepository, KafkaProducer kafkaProducer) {
        this.productRepository = productRepository;
        this.kafkaProducer = kafkaProducer;
    }

    @Cacheable(value = "productsCache", key = "#id", unless = "#result==null")
    public Product getProductById(String id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found !"));
    }

    @Cacheable(value = "productsCache", key = "#name", unless = "#result==null")
    public Product getProductByName(String name){
        return productRepository.findProductByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Product not found "));
    }

    @Cacheable("productsCache")
    public List<Product> getAllProducts(){
        log.info("Get all products");
        return (List<Product>) productRepository.findAll();
    }

    public boolean isProductExistById(String id){
        return productRepository.existsById(id);
    }

    @CachePut(value = "productsCache", key = "#product.id")
    public Product save(Product product){
        Product savedProduct = productRepository.save(product);
        kafkaProducer.sendProduct(savedProduct);
        return savedProduct;
    }

    @CacheEvict(value = "productsCache", key = "#id")
    public void deleteProductById(String id){
        productRepository.deleteById(id);
        log.debug("Product {} is deleted.", id);
    }
}
