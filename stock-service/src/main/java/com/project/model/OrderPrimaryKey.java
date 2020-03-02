package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderPrimaryKey implements Serializable {

    private String userEmail;

    private String orderId;

    private String orderTime;

    private String shippingTime;

}
