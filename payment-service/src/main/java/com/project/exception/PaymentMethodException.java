package com.project.exception;

public class PaymentMethodException extends RuntimeException{

    public PaymentMethodException(String message) {
        super(message);
    }

    public PaymentMethodException(String message, Throwable cause) {
        super(message, cause);
    }
}
