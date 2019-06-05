package com.project.client;

import com.project.exception.ProductNotFoundException;
import com.project.model.Product;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductClientFallback implements ProductClient {

    private final Throwable throwable;

    public ProductClientFallback(Throwable throwable) {
        this.throwable = throwable;
    }


    @Override
    public Product getProductByName(String name) {
        log.info("fallback method for getProductByName ");
        return new Product();
    }

    @Override
    public Product getProductById(String id) {
        if (throwable instanceof ProductNotFoundException){
            throw new ProductNotFoundException(throwable.getCause().getMessage());
        }
        log.info("fallback method");
        return new Product();
    }


    @Override
    public Product createOrUpdateProduct(Product product) {
        return null;
    }
}
