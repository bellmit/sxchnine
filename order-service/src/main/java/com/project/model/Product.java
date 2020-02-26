package com.project.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.math.BigDecimal;

@UserDefinedType("product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Product {

    @Column("product_name")
    private String productName;

    @Column("product_brand")
    private String productBrand;

    @Column("image")
    private String image;

    @Column("product_id")
    private String productId;

    @Column("product_size")
    private String productSize;

    @Column("product_color")
    private String productColor;

    @Column("product_qte")
    private int productQte;

    @Column("unit_price")
    private BigDecimal unitPrice;

    @Column("store")
    private String store;
}
