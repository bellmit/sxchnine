package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order implements Serializable {

    private String orderId;
    private String userEmail;
    @JsonIgnore
    private String orderTime;
    private String productBrand;
    private String productName;
    private BigDecimal total;
    private Address userAddress;
    private String orderStatus;
    @JsonIgnore
    private String paymentTime;
    private String shippingStatus;
}
