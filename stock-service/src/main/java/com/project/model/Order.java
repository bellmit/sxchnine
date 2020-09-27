package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order implements Serializable {

    private OrderPrimaryKey orderPrimaryKey;
    @JsonProperty(value ="products")
    private List<ProductOrder> productOrders;
    private String productBrand;
    private String productName;
    private BigDecimal total;
    private PaymentInfo paymentInfo;
    private Address userAddress;
    private String orderStatus;
    private String paymentStatus;
    private String paymentTime;
    private String shippingStatus;

}
