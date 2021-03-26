package com.project.utils;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentStatusCodeTest {

    @Test
    public void testGetStatusByCode() {
        assertEquals("REFUSED", PaymentStatusCode.getStatusByCode(0));
        assertEquals("CONFIRMED", PaymentStatusCode.getStatusByCode(1));
        assertEquals("WAITING", PaymentStatusCode.getStatusByCode(2));
        assertEquals("REQUIRED_ACTION", PaymentStatusCode.getStatusByCode(3));
    }
}
