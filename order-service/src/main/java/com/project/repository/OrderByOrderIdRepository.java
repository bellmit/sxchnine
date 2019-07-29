package com.project.repository;

import com.project.model.OrderId;
import com.project.model.OrderPrimaryKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderByOrderIdRepository extends CrudRepository<OrderId, OrderPrimaryKey> {

    OrderId findOrderIdByOrderIdPrimaryKeyOrderId(UUID orderId);
}
