package com.project.controller;

import com.project.business.ProductService;
import com.project.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/search/{value}")
    public Flux<Product> searchProduct(@PathVariable String value) {
        return productService.getProductsByQuery(value);
    }

    @GetMapping(value = "/advancedSearch")
    public Flux<Product> advancedSearchProduct(@RequestParam(required = false) String gender,
                                               @RequestParam(required = false) String brand,
                                               @RequestParam(required = false) String category,
                                               @RequestParam(required = false) String size) {
        return productService.getProductsByAdvancedFiltering(gender, brand, category, size);
    }

    @PostMapping("/save")
    public Mono<Void> saveProducts(@RequestBody Product product) {
        return productService.save(product);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id) {
        return productService.deleteById(id);
    }
}
