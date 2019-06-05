package com.project.repository;

import com.project.model.Order;
import com.project.model.OrderPrimaryKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends CrudRepository<Order, OrderPrimaryKey> {

    List<Order> findAll();

    List<Order> findOrdersByOrderPrimaryKeyUserEmail(String userEmail);

    List<Order> findOrdersByOrderPrimaryKeyUserEmailAndOrderPrimaryKeyOrderId(String email, UUID orderId);

}
