package com.project.business;

import com.project.model.Product;
import com.project.utils.PromotionCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class Promotion10 implements PromotionStrategy {

    @Override
    public int getPromotion() {
        return 10;
    }

    @Override
    public List<Product> applyPromotion(List<Product> products) {
        return products.stream()
                .peek(p -> {
                    p.setPromotion(10);
                    p.setOriginalPrice(p.getPrice());
                    p.setPrice(PromotionCalculator.calculatePromotion(p.getPrice(), 10));
                })
                .peek((p -> log.info("apply 10 promo on {}", p.getName())))
                .collect(Collectors.toList());
    }

}
