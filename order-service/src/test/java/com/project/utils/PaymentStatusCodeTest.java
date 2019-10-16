package com.project.utils;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;

public class PaymentStatusCodeTest {

    String var = " Hello Mounir";

    @Test
    public void testGetStatusByCode(){
        assertEquals("REFUSED", PaymentStatusCode.getStatusByCode(0));
        assertEquals("CONFIRMED", PaymentStatusCode.getStatusByCode(1));
        assertEquals("WAITING", PaymentStatusCode.getStatusByCode(2));
        assertEquals("UNKNOWN", PaymentStatusCode.getStatusByCode(3));
    }

    @Test
    public void test(){
        System.out.println(Thread.currentThread().getName() + var);
        CompletableFuture.runAsync(() -> {
            var = " Hello Hamidine";
            System.out.println(Thread.currentThread().getName() + var);
        }).thenAccept(v -> set(" Hello pop"));

        System.out.println(Thread.currentThread().getName() + var);

    }

    public CompletableFuture<String> set(String var){
        this.var = var;
        return null;
    }
}
