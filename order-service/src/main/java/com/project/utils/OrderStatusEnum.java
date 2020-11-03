package com.project.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum OrderStatusEnum {
    ORDERED("ORDERED"),
    PROCESSING("PROCESSING"),
    PREPARE_TO_SHIP("PREPARING"),
    SHIPPED("SHIPPED");

    private String value;
}
