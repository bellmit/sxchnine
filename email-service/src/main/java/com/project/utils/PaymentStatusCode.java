package com.project.utils;

public enum PaymentStatusCode {
    OK("Confirmed"),
    NOK("Refused"),
    WAITING("Waiting");

    private String value;

    PaymentStatusCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
