package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
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
