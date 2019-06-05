package com.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "orders_idx", type = "order", createIndex = false)
public class IndexedOrder {

    @Id
    private String id;
    private String userEmail;
    private String orderId;
    private String orderTime;
    private String productName;
    private String productBrand;
    private String productId;
    private String productSize;
    private String productColor;
    private int productQte;
    private BigDecimal unitPrice;
    private String store;
    private Address userAddress;
    private String orderStatus;
    private String paymentStatus;
    private String paymentTime;
    private String shippingStatus;
    private String shippingTime;
}
