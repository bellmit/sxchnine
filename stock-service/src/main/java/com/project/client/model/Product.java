package com.project.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.model.SizeQte;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product implements Serializable {

    private String id;
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
