package com.project.client;

import com.project.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service", fallbackFactory = ProductClientFallbackFactory.class)
public interface ProductClient {

    @GetMapping("/id/{id}")
    Product getProductById(@PathVariable("id") String id);

    @GetMapping("/name/{name}")
    Product getProductByName(@PathVariable("name") String name);

    @PostMapping("/save")
    Product createOrUpdateProduct(@RequestBody Product product);
}
