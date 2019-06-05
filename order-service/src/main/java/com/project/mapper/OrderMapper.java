package com.project.mapper;

import com.project.model.Order;
import com.project.model.OrderId;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = OrderPrimaryKeyMapper.class)
public interface OrderMapper {

    @Mapping(target = "orderPrimaryKey", source = "orderIdPrimaryKey")
    Order asOrder(OrderId orderId);

    @InheritInverseConfiguration
    OrderId asOrderId(Order order);
}
