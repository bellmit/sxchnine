package com.project.model;

import lombok.*;

import java.io.Serializable;

@Data
public class OrderPrimaryKey implements Serializable {

    private String userEmail;

    private String orderId;

    private String orderTime;

    private String shippingTime;

}
