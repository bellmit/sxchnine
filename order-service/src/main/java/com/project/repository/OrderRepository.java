package com.project.repository;

import com.project.model.Order;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, String> {

    Flux<Order> findAll();

    Flux<Order> findOrdersByUserEmail(String userEmail);

    Mono<Order> findOrderByOrderId(String orderId);

    Mono<Order> findOrderByOrderIdAndUserEmail(String orderId, String email);

    Flux<Order> findOrdersByOrderTimeGreaterThanEqual(LocalDateTime month);
}
