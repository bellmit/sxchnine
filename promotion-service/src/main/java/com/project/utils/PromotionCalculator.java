package com.project.utils;

public class PromotionCalculator {

    public static double calculatePromotion(double price, int promotion){
        return price - (price * (promotion / 100));
    }
}
