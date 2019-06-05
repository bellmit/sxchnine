package com.project.utils;

public enum PaymentStatusCode {
    CONFIRMED("CONFIRMED"),
    REFUSED("REFUSED"),
    WAITING("WAITING");

    private String value;

    PaymentStatusCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
