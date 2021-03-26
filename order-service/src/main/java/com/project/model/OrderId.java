package com.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class OrderId {

    private OrderIdKey orderIdKey;
    private String orderStatus;
    private List<Product> products;
    private String productBrand;
    private String productName;
    private BigDecimal total;
    private PaymentInfo paymentInfo;
    private Address userAddress;
    private String paymentStatus;
    private LocalDateTime paymentTime;
    private String shippingStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime shippingTime;
    private String trackingNumber;

}
