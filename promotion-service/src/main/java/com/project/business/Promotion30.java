package com.project.business;

import com.project.model.Product;
import com.project.utils.PromotionCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class Promotion30 implements PromotionStrategy {

    @Override
    public int getPromotion() {
        return 30;
    }

    @Override
    public List<Product> applyPromotion(List<Product> products) {
        return products.stream()
                .peek(p -> {
                    p.setPromotion(30);
                    p.setOriginalPrice(p.getPrice());
                    p.setPrice(PromotionCalculator.calculatePromotion(p.getPrice(), 30));
                })
                .peek((p -> log.info("apply 30 promo on {}", p.getName())))
                .collect(Collectors.toList());
    }
}
