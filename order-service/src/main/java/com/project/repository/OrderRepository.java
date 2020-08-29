package com.project.repository;

import com.project.model.Order;
import com.project.model.OrderPrimaryKey;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderRepository extends ReactiveCassandraRepository<Order, OrderPrimaryKey> {

    Flux<Order> findAll();

    Flux<Order> findOrdersByOrderPrimaryKeyUserEmail(String userEmail);

    Flux<Order> findOrdersByOrderPrimaryKeyUserEmailAndOrderPrimaryKeyOrderId(String email, String orderId);

}
