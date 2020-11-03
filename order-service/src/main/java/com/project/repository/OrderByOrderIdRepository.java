package com.project.repository;

import com.project.model.OrderId;
import com.project.model.OrderKey;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderByOrderIdRepository extends ReactiveCassandraRepository<OrderId, OrderKey> {

    Mono<OrderId> findOrderIdByOrderIdKeyOrderId(String orderId);

    Flux<OrderId> findOrderIdByOrderIdKeyOrderIdAndOrderIdKeyUserEmail(String orderId, String email);
}
