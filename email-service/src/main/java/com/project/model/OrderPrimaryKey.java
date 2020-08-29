package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class OrderPrimaryKey implements Serializable {

    private String orderId;
    private String userEmail;
    private String productName;
    private String productBrand;
    @JsonIgnore
    private String orderTime;
    @JsonIgnore
    private String shippingTime;
}
