package com.project.repository;

import com.project.model.OrderId;
import com.project.model.OrderPrimaryKey;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface OrderByOrderIdRepository extends ReactiveCassandraRepository<OrderId, OrderPrimaryKey> {

    Mono<OrderId> findOrderIdByOrderIdPrimaryKeyOrderId(String orderId);
}
