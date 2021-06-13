package com.project.controller;


import com.project.business.ProductService;
import com.project.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.cloud.sleuth.instrument.web.WebFluxSleuthOperators.withSpanInScope;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping(value = "/id/{id}")
    public Mono<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping(value = "/ids", produces = APPLICATION_JSON_VALUE)
    public Flux<Product> getProductsByIds(@RequestParam List<Long> ids) {
        return productService.getProductByIds(ids);
    }

    @GetMapping("/name/{name}")
    public Mono<Product> getProductByName(@PathVariable String name) {
        return productService.getProductByName(name);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Product> getProducts(@RequestParam(defaultValue = "0") int pageNo,
                                     @RequestParam(defaultValue = "2") int pageSize) {
        return productService.getAllProducts();
    }

    @GetMapping(value = "/allBySex", produces = APPLICATION_JSON_VALUE)
    public Flux<Product> getProductsBySex(@RequestParam(defaultValue = "0") int pageNo,
                                          @RequestParam(defaultValue = "2") int pageSize,
                                          @RequestParam char sex) {
        return productService.getAllProductsBySex(pageNo, pageSize, sex);
    }

    @GetMapping("/admin/searchProducts")
    public Flux<Product> searchProducts(@RequestParam(required = false) Long id,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) String brand,
                                        @RequestParam(required = false) String sex) {
        return productService.searchProducts(id, name, brand, sex)
                .doOnEach(withSpanInScope(SignalType.ON_NEXT, signal -> log.info("Search products with param: id:{} - name:{} - brand:{} - gender:{}", id, name, brand, sex)));
    }

    @PostMapping("/save")
    public Mono<Product> createOrUpdateProduct(@RequestBody Product product) {
        return productService.save(product);
    }

    @PostMapping(value = "/bulk", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> createOrUpdateProducts(@RequestBody List<Product> products) {
        return productService.saveProducts(products);
    }

    @DeleteMapping("/delete/id/{id}")
    public Mono<Void> deleteProductById(@PathVariable Long id) {
        return productService.deleteProductById(id);
    }

    @PostMapping("/repairAvailability")
    public Mono<Void> repair(){
        return productService.getAllProducts().flatMap(p -> {
            p.getAvailability().keySet().removeIf(t -> Pattern.compile("([\"])\\w+").asPredicate().test(t));
            return productService.save(p);
        }).then();
    }
}
