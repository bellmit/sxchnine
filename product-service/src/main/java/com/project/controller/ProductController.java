package com.project.controller;


import com.project.business.ProductService;
import com.project.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/id/{id}")
    public Product getProductById(@PathVariable String id){
        log.info("Get Product {}", id);
        return productService.getProductById(id);
    }

    @GetMapping("/ids")
    public List<Product> getProductsByIds(@RequestParam List<String> ids){
        log.info("Get Products by ids {}", ids);
        return productService.getProductByIds(ids);
    }

    @GetMapping("/name/{name}")
    public Product getProductByName(@PathVariable String name){
        log.info("Get Product {}", name);
        return productService.getProductByName(name);
    }

    @GetMapping("/all")
    public List<Product> getProducts(@RequestParam(defaultValue = "0") int pageNo,
                                     @RequestParam(defaultValue = "2") int pageSize){
        return productService.getAllProducts(pageNo, pageSize);
    }

    @GetMapping("/allBySex")
    public List<Product> getProductsBySex(@RequestParam(defaultValue = "0") int pageNo,
                                          @RequestParam(defaultValue = "2") int pageSize,
                                          @RequestParam char sex){
        return productService.getAllProductsBySex(pageNo, pageSize, sex);
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

    @PostMapping("/bulk")
    public void createOrUpdateProducts(@RequestBody List<Product> products){
        productService.saveProducts(products);
    }

    @DeleteMapping("/delete/id/{id}")
    public void deleteProductById(@PathVariable String id){
        productService.deleteProductById(id);
    }

}
