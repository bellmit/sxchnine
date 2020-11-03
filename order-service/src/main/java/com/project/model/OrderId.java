package com.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Table("orders_by_orderId")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class OrderId {

    @PrimaryKey
    private OrderIdKey orderIdKey;

    @Column("order_status")
    private String orderStatus;

    @Column("products")
    private List<Product> products;

    @Column("product_brand")
    private String productBrand;

    @Column("product_name")
    private String productName;

    @Column("total")
    private BigDecimal total;

    @Column("payment_info")
    private PaymentInfo paymentInfo;

    @Column("user_address")
    private Address userAddress;

    @Column("payment_status")
    private String paymentStatus;

    @Column("payment_time")
    private LocalDateTime paymentTime;

    @Column("shipping_status")
    private String shippingStatus;

    @Column("shipping_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime shippingTime;

    @Column("tracking_number")
    private String trackingNumber;

}
