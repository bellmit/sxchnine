package com.project.utils;

public enum ProductCode {

    PRODUCT_FALLBACK("productFallback");

    private String value;

    ProductCode(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
