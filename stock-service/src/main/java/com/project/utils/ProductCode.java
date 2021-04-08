package com.project.utils;

public enum ProductCode {

    PRODUCT_FALLBACK(9999999999999L);

    private Long value;

    ProductCode(Long value) {
        this.value = value;
    }

    public Long getValue(){
        return value;
    }
}
