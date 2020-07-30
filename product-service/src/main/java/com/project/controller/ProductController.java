package com.project.controller;


import com.project.business.ProductService;
import com.project.model.Product;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/id/{id}")
    public Mono<Product> getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping(value = "/ids", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Product> getProductsByIds(@RequestParam List<String> ids){
        return productService.getProductByIds(ids);
    }

    @GetMapping("/name/{name}")
    public Mono<Product> getProductByName(@PathVariable String name){
        return productService.getProductByName(name);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Product> getProducts(@RequestParam(defaultValue = "0") int pageNo,
                                     @RequestParam(defaultValue = "2") int pageSize){
        return productService.getAllProducts();
    }

    @GetMapping(value = "/allBySex", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Product> getProductsBySex(@RequestParam(defaultValue = "0") int pageNo,
                                          @RequestParam(defaultValue = "2") int pageSize,
                                          @RequestParam char sex){
        return productService.getAllProductsBySex(pageNo, pageSize, sex);
    }

    @PostMapping("/save")
    public Mono<Product> createOrUpdateProduct(@RequestBody Product product){
        return productService.save(product);
    }

    @PostMapping("/bulk")
    public Mono<Void> createOrUpdateProducts(@RequestBody Flux<Product> products){
        return productService.saveProducts(products);
    }

    @DeleteMapping("/delete/id/{id}")
    public Mono<Void> deleteProductById(@PathVariable Long id){
        return productService.deleteProductById(id);
    }

}
