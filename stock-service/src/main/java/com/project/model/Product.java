package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
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
