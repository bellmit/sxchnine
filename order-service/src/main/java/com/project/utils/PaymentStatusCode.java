package com.project.utils;

public enum PaymentStatusCode {
    REFUSED(0, "REFUSED"),
    CONFIRMED(1, "CONFIRMED"),
    WAITING(2,"WAITING"),
    REQUIRED_ACTION(3,"REQUIRED_ACTION"),
    UNKNOWN(4, "UNKNOWN");

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

    public static String getStatusByCode(int code){
        for (PaymentStatusCode paymentStatusCode : PaymentStatusCode.values()){
            if (paymentStatusCode.getCode() == code){
                return paymentStatusCode.getValue();
            }
        }
        return UNKNOWN.getValue();
    }

    public static int getStatusByValue(String value){
        for (PaymentStatusCode paymentStatusCode : PaymentStatusCode.values()){
            if (paymentStatusCode.getValue().equals(value)){
                return paymentStatusCode.getCode();
            }
        }
        return UNKNOWN.getCode();
    }
}
