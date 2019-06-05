package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order implements Serializable {

    private OrderPrimaryKey orderPrimaryKey;
    private String productId;
    private String productSize;
    private String productColor;
    private int productQte;
    private Address userAddress;
    private float unitPrice;
    private String orderStatus;
    private String paymentStatus;
    @JsonIgnore
    private String paymentTime;
    private String shippingStatus;
}
