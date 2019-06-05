package com.project.business;

import com.project.model.Product;

import java.util.List;

public interface PromotionStrategy {

    int getPromotion();
    List<Product> applyPromotion(List<Product> products);
}
