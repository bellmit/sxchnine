package com.project.mapper;

import com.project.model.OrderIdKey;
import com.project.model.OrderKey;
import org.mapstruct.Mapper;

@Mapper
public interface OrderKeyMapper {

    OrderKey asOrderPrimaryKey(OrderIdKey orderIdKey);

    OrderIdKey asOrderIdPrimaryKey(OrderKey orderKey);
}
