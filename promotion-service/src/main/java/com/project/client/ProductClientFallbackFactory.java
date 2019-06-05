package com.project.client;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ProductClientFallbackFactory implements FallbackFactory<ProductClient> {


    @Override
    public ProductClient create(Throwable throwable) {
        return new ProductClientFallback(throwable);
    }
}
