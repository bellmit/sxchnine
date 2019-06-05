package com.project.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;

@Document(collection = "product")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {

    @Id
    private String id;
    private String reference;
    private String name;
    private String brand;
    private String category;
    private BigDecimal price;
    private String size;
    private String color;
    private String image;
    private Dimension dimension;
    private BigDecimal originalPrice;
    private int promotion;
    private String store;
    private String dateTime;

}
