package com.project.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.time.LocalDateTime;

import static org.springframework.data.cassandra.core.cql.Ordering.ASCENDING;
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
public class OrderIdKey {

    @PrimaryKeyColumn(name = "order_id", type = PARTITIONED, ordinal = 0)
    private String orderId;

    @PrimaryKeyColumn(name = "user_email", type = CLUSTERED, ordinal = 1, ordering = ASCENDING)
    private String userEmail;

    @PrimaryKeyColumn(name = "order_time", type = CLUSTERED, ordinal = 2, ordering = DESCENDING)
    private LocalDateTime orderTime;
}
