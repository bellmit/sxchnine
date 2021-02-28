package com.project.utils;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;

public class PaymentStatusCodeTest {

    String var = " Hello Mounir";

    @Test
    public void testGetStatusByCode(){
        assertEquals("REFUSED", PaymentStatusCode.getStatusByCode(0));
        assertEquals("CONFIRMED", PaymentStatusCode.getStatusByCode(1));
        assertEquals("WAITING", PaymentStatusCode.getStatusByCode(2));
        assertEquals("REQUIRED_ACTION", PaymentStatusCode.getStatusByCode(3));
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

    @Test
    public void testShift(){
        int[] tab = {0, 4, 6, 7, 8, 0, 0};

        int [] newTab = new int [tab.length + 5];

        for (int i = 0; i < tab.length; i++){
            newTab[i] = tab[i];
        }

        int mid = 1;
        for (int j = newTab.length - 2; j > mid ;j--){
            newTab[j + 1] = newTab[j];
        }

        newTab[mid+1] = 5;

        Arrays.stream(newTab).forEach(System.out::println);
    }
}
