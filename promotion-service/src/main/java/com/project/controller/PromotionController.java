package com.project.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.project.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @PostMapping("applyPromotion/{promotion}/productId/{productId}")
    public void applyPromotion(@PathVariable int promotion, @PathVariable String productId){
        promotionService.processPromotion(promotion, productId);
    }

    @PostMapping("applyPromotionByName/{promotion}/productId/{name}")
    public void applyPromotionByName(@PathVariable int promotion, @PathVariable String name){
        promotionService.processPromotionByName(promotion, name);
    }
}
