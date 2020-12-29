package com.project.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderProcessingStatus {

    ORDER_PAYMENT_PROCESS(1, "PAYMENT_START_PROCESS"),
    ORDER_PAYMENT_PROCESSED(2, "PAYMENT_PROCESSED"),
    ORDER_PAYMENT_ERROR(3, "PAYMENT_ERROR"),
    ORDER_SAVE_SUCCESS(4, "SAVE_SUCCESS"),
    ORDER_SAVE_ERROR(5, "SAVE_ERROR");

    private int code;
    private String Value;
}
