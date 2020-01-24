package com.project.model;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Document(indexName = "store_products", type = "_doc", createIndex = false)
public class Product implements Serializable {

    private String id;
    private String reference;
    private String name;
    private String brand;
    private String sex;
    private String category;
    private double price;
    private List<String> size;
    private List<String> colors;
    private String logo;
    private List<String> images;
    private Dimension dimension;
    private double originalPrice;
    private int promotion;
    private String store;
    private String dateTime;
}
