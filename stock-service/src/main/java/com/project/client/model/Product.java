package com.project.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.model.SizeQte;
import lombok.*;

import java.io.Serializable;
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
    private String name;
    private char sex;
    private Set<String> size;
    private Set<String> colors;
    private Set<String> images;
    private Map<String, Set<SizeQte>> availability;
    private boolean available;
    private int quantity;
}
