package com.project.repository;

import com.project.model.OrderStatus;
import com.project.model.OrderStatusKey;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderStatusRepository extends ReactiveCassandraRepository<OrderStatus, OrderStatusKey> {

    Flux<OrderStatus> findOrderStatusesByOrderStatusKeyBucket(String bucket);

}
