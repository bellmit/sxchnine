package com.project.utils;

public enum PaymentStatusCode {
    REFUSED(0, "REFUSED"),
    CONFIRMED(1, "CONFIRMED"),
    WAITING(2,"WAITING"),
    REQUIRED_ACTION(3,"REQUIRED_ACTION"),
    REQUIRED_CONFIRMATION(4,"REQUIRED_CONFIRMATION"),
    UNKNOWN(5, "UNKNOWN");

    private final int code;
    private final String value;

    PaymentStatusCode(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() { return code; }

    public String getValue() {
        return value;
    }
}
