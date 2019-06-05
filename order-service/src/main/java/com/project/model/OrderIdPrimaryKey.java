package com.project.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.data.cassandra.core.cql.Ordering.DESCENDING;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.CLUSTERED;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;

@PrimaryKeyClass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class OrderIdPrimaryKey {

    @PrimaryKeyColumn(name = "order_id", type = PARTITIONED, ordinal = 0)
    private UUID orderId;

    @PrimaryKeyColumn(name = "user_email", type = CLUSTERED, ordinal = 1)
    private String userEmail;

    @PrimaryKeyColumn(name = "order_time", type = CLUSTERED, ordinal = 2, ordering = DESCENDING)
    private LocalDateTime orderTime;

    @PrimaryKeyColumn(name = "shipping_time", type = CLUSTERED, ordinal = 3, ordering = DESCENDING)
    private LocalDateTime shippingTime;

}
