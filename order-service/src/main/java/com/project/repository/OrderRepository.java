package com.project.repository;

import com.project.model.Order;
import com.project.model.OrderKey;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderRepository extends ReactiveCassandraRepository<Order, OrderKey> {

    Flux<Order> findAll();

    Flux<Order> findOrdersByOrderKeyUserEmail(String userEmail);
}
