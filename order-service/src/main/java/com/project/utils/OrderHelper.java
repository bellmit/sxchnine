package com.project.utils;

import static com.project.utils.PaymentStatusCode.*;

public class OrderHelper {

    private static final String TIMEOUT = "TIMEOUT";

    public static String evaluateStatus(String status){
        if (status.equalsIgnoreCase(CHECKOUT_OP.getValue())
                || status.equalsIgnoreCase(CONFIRM_OP.getValue())
                || status.contains(TIMEOUT)){
            return WAITING.getValue();
        } else {
            return status;
        }
    }
}
