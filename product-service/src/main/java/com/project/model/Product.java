package com.project.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Document(collection = "product")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {

    @Id
    private Long id;
    private String reference;
    private String name;
    private char sex;
    private String brand;
    private String logo;
    private String category;
    private BigDecimal price;
    private Set<String> size;
    private Set<String> colors;
    private Set<String> images;
    private Map<String, Set<SizeQte>> availability;
    private boolean available;
    private int quantity;
    private Dimension dimension;
    private BigDecimal originalPrice;
    private int promotion;
    private String store;
    private String dateTime;

}
