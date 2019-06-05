package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderPrimaryKey implements Serializable {

    private UUID orderId;
    private String userEmail;
    private String productName;
    private String productBrand;
    @JsonIgnore
    private String orderTime;
    @JsonIgnore
    private String shippingTime;
}
