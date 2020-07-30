package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.List;

@Data
@Document(indexName = "store_products", createIndex = false)
@JsonIgnoreProperties(ignoreUnknown = true)
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
