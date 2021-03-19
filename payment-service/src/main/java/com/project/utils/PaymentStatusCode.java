package com.project.utils;

public enum PaymentStatusCode {
    REFUSED(0, "REFUSED"),
    CONFIRMED(1, "CONFIRMED"),
    WAITING(2,"WAITING"),
    REQUIRED_ACTION(3,"REQUIRED_ACTION"),
    REQUIRED_CONFIRMATION(4,"REQUIRED_CONFIRMATION"),
    UNKNOWN(5, "UNKNOWN"),
    CHECKOUT_OP(6, "CHECKOUT_CALL_ERROR"),
    CONFIRM_OP(7, "CONFIRM_CALL_ERROR"),
    WAITING_TIMEOUT(8, "WAITING_TIMEOUT"),
    CALL_PAYMENT_METHOD_TIMEOUT(9, "CALL_PAYMENT_METHOD_TIMEOUT"),
    CREATE_PAYMENT_INTENT_TIMEOUT(10, "CREATE_PAYMENT_INTENT_TIMEOUT"),
    RETRIEVE_PAYMENT_INTENT_TIMEOUT(11, "RETRIEVE_PAYMENT_INTENT_TIMEOUT"),
    CONFIRM_PAYMENT_TIMEOUT(12, "CONFIRM_PAYMENT_TIMEOUT");

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
