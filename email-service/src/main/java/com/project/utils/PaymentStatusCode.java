package com.project.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentStatusCode {
    REFUSED(0, "REFUSED"),
    CONFIRMED(1, "CONFIRMED"),
    WAITING(2,"WAITING"),
    UNKNOWN(3, "UNKNOWN"),
    SHIPPED(4, "SHIPPED"),
    CONTACT(5, "CONTACT");

    private final int code;
    private final String value;


    public static String getStatusByCode(int code){
        for (PaymentStatusCode paymentStatusCode : PaymentStatusCode.values()){
            if (paymentStatusCode.getCode() == code){
                return paymentStatusCode.getValue();
            }
        }

        return UNKNOWN.getValue();
    }
}
