package com.project.controller;


import com.project.business.ProductService;
import com.project.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/id/{id}")
    public Product getProductById(@PathVariable String id){
        log.info("Get Product {}", id);
        return productService.getProductById(id);
    }

    @GetMapping("/name/{name}")
    public Product getProductByName(@PathVariable String name){
        log.info("Get Product {}", name);
        return productService.getProductByName(name);
    }

    @GetMapping("/all")
    public List<Product> getProducts(){
        return productService.getAllProducts();
    }

    @PostMapping("/save")
    public Product createOrUpdateProduct(@RequestBody Product product){
/*        Product p = new Product();
        p.setId("A2");
        p.setName("Carhartt mid 90's");
        p.setBrand("Carhartt");
        p.setColor("Black");
        p.setPrice(BigDecimal.valueOf(100));
        p.setOriginalPrice(BigDecimal.valueOf(100));
        p.setSize(String.valueOf(42));
        p.setCategory("Jacket");
        p.setReference("A2");
        p.setStore("CA");
        p.setDimension(new Dimension());
        p.getDimension().setWidth(8);
        p.getDimension().setLength(43);
        p.getDimension().setHeight(1);
        p.setDateTime(LocalDateTime.now().toString());*/
        return productService.save(product);
    }

    @DeleteMapping("/delete/id/{id}")
    public void deleteProductById(@PathVariable String id){
        productService.deleteProductById(id);
    }

}
