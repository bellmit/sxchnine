package com.project.model;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class Product {

    private String productName;

    private String productBrand;

    private String productId;

    private String productSize;

    private String productColor;

    private int productQte;

    private BigDecimal unitPrice;

    private String store;

}
