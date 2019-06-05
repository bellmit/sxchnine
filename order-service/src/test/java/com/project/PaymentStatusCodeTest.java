package com.project;

import com.project.utils.PaymentStatusCode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PaymentStatusCodeTest {

    @Test
    public void testGetStatusByCode(){
        assertEquals("REFUSED", PaymentStatusCode.getStatusByCode(0));
        assertEquals("CONFIRMED", PaymentStatusCode.getStatusByCode(1));
        assertEquals("WAITING", PaymentStatusCode.getStatusByCode(2));
        assertEquals("UNKNOWN", PaymentStatusCode.getStatusByCode(3));
    }
}
