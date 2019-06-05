package com.project.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String id;
    private String reference;
    private String name;
    private String brand;
    private String category;
    private double price;
    private String size;
    private String color;
    private String image;
    private double originalPrice;
    private int promotion;
}
