package com.project.utils;

import static com.project.utils.PaymentStatusCode.*;

public class OrderHelper {

    public static String evaluateStatus(String status){
        if (status.equalsIgnoreCase(CHECKOUT_OP.getValue())
                || status.equalsIgnoreCase(CONFIRM_OP.getValue())
                || status.equalsIgnoreCase(WAITING_TIMEOUT.getValue())){
            return WAITING.getValue();
        } else {
            return status;
        }
    }
}
