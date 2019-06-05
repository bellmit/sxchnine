package com.project.model;

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
    private OrderIdPrimaryKey orderIdPrimaryKey;

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

    @Column("order_status")
    private String orderStatus;

    @Column("payment_status")
    private String paymentStatus;

    @Column("payment_time")
    private LocalDateTime paymentTime;

    @Column("shipping_status")
    private String shippingStatus;

}
