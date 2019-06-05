package com.project.repository;

import com.project.model.OrderId;
import com.project.model.OrderPrimaryKey;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OrderByOrderIdRepository extends CrudRepository<OrderId, OrderPrimaryKey> {

    OrderId findOrderIdByOrderIdPrimaryKeyOrderId(UUID orderId);
}
