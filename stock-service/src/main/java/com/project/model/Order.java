package com.project.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Order {

    private OrderPrimaryKey orderPrimaryKey;

    private List<Product> products;

    private String productBrand;

    private String productName;

    private BigDecimal total;

    private PaymentInfo paymentInfo;

    private Address userAddress;

    private String orderStatus;

    private String paymentStatus;

    private String paymentTime;

    private String shippingStatus;

}
