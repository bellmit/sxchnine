package com.project.utils;

public enum PaymentStatusCode {
    REFUSED(0, "REFUSED"),
    CONFIRMED(1, "CONFIRMED"),
    WAITING(2,"WAITING"),
    UNKNOWN(3, "UNKNOWN"),
    CONTACT(4, "CONTACT");

    private int code;
    private String value;

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
}
