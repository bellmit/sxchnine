package com.project.mapper;

import com.project.model.OrderIdKey;
import com.project.model.OrderKey;
import com.project.model.OrderStatusKey;
import org.mapstruct.Mapper;

@Mapper
public interface OrderStatusKeyMapper {

    OrderKey asOrderPrimaryKey(OrderStatusKey orderStatusKey);

    OrderStatusKey asOrderStatusPrimaryKeyByOrder(OrderKey orderKey);

    OrderIdKey asOrderIdPrimaryKey(OrderStatusKey orderStatusKey);

    OrderStatusKey asOrderStatusPrimaryKeyByOrderId(OrderIdKey orderIdKey);



}
