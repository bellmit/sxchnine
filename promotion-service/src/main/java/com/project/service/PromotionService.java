package com.project.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.project.business.PromotionStrategy;
import com.project.client.ProductClient;
import com.project.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class PromotionService  {

    @Autowired
    private PromotionContext promotionContext;

    @Autowired
    private ProductClient productClient;

    //@HystrixCommand(fallbackMethod = "processPromotionDefault")
    public void processPromotion(int promotion, String productId){
        Product product = productClient.getProductById(productId);
        log.info("Product found {}", product);
        promotionContext.getStrategy(promotion).applyPromotion(Arrays.asList(product))
                .stream()
                .forEach(p -> productClient.createOrUpdateProduct(p));
    }

    public void processPromotionByName(int promotion, String name){
        Product product = productClient.getProductByName(name);
        log.info("Product found {}", product);
        promotionContext.getStrategy(promotion).applyPromotion(Arrays.asList(product))
                .stream()
                .forEach(p -> productClient.createOrUpdateProduct(p));
    }

    public void processPromotionDefault(int promotion, String productId){
        log.info("fallback method for getProductById");
        promotionContext.getStrategy(promotion).applyPromotion(Arrays.asList(new Product()));
    }
}
