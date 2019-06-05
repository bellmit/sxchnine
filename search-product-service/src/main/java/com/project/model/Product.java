package com.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Document(indexName = "store_products", type = "product", createIndex = false)
public class Product implements Serializable {

    private String id;
    private String reference;
    private String name;
    private String brand;
    private String category;
    private double price;
    private String size;
    private String color;
    private String image;
    private Dimension dimension;
    private double originalPrice;
    private int promotion;
    private String store;
    private String dateTime;
}
