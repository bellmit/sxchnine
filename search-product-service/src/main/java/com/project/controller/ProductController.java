package com.project.controller;

import com.project.business.ProductService;
import com.project.model.Dimension;
import com.project.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

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
        Product product1 = new Product();
        product1.setReference("REF2");
        product1.setName("Classic Girl Bomber");
        product1.setSex("W");
        product1.setBrand("Carhartt");
        product1.setCategory("Jacket");
        product1.setPrice(200);
        product1.setSize(Arrays.asList("S", "M", "L"));
        product1.setColors(Arrays.asList("RED", "White"));
        product1.setImages(Collections.singletonList("https://i1.adis.ws/i/carhartt_wip/I025115_05V_90-ST-01/w-deming-jacket-panther-print-blast-red-black-black-1739.png?$pdp_zoom$"));
        product1.setLogo("https://imgcdn.carhartt.com/is/image/Carhartt/carhartt-logo-footer?$footer-logo-retina$");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateParsedAsString = now.format(formatter);
        log.info("-------------------- {}", dateParsedAsString);
        log.info("-------------------- {}", LocalDateTime.parse(dateParsedAsString, formatter));
        product1.setDateTime(dateParsedAsString);
        Dimension dimension = new Dimension();
        dimension.setHeight(100);
        dimension.setLength(100);
        dimension.setWidth(50);
        product1.setDimension(dimension);
        return productService.save(product);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id) {
        return productService.deleteById(id);
    }
}
