package com.project.controller;

import com.project.business.ProductService;
import com.project.model.Dimension;
import com.project.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/search/all")
    public List<Product> searchAllProduct() {
        return StreamSupport.stream(productService.getAllProducts().spliterator(), true).collect(Collectors.toList());
    }

    @GetMapping("/search/{value}")
    public List<Product> searchProduct(@PathVariable String value) {
        return StreamSupport.stream(productService.getProductsByQuery(value).spliterator(), true).collect(Collectors.toList());
    }

    @GetMapping("/advancedSearch/{brand}/{category}/{size}/")
    public List<Product> advancedSearchProduct(@PathVariable Optional<String> brand, @PathVariable Optional<String> category, @PathVariable Optional<String> size) {
        Iterable<Product> products = productService.getProductsByAdvancedFiltering(brand.orElse(null), category.orElse(null), size.orElse(null));

        return StreamSupport.stream(products.spliterator(), false).collect(Collectors.toList());
    }

    @PostMapping("/save")
    public void saveProducts(@RequestBody Product product) {
        Product product1 = new Product();
        product1.setReference("REF1");
        product1.setName("Classic retro 90's Bomber");
        product1.setBrand("nike");
        product1.setCategory("Jacket");
        product1.setPrice(100);
        product1.setSize("M");
        product1.setColor("Black");
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
        productService.save(product);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable String id) {
        productService.deleteById(id);
    }
}
