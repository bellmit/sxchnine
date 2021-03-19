package com.project.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Product {

    private String productId;
    private String productName;
    private String productBrand;
    private String image;
    private String productSize;
    private String productColor;
    private Integer productQte;
    private BigDecimal unitPrice;
    private String store;
}
