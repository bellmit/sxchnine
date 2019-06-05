package com.project.mapper;

import com.project.model.OrderIdPrimaryKey;
import com.project.model.OrderPrimaryKey;
import org.mapstruct.Mapper;

@Mapper
public interface OrderPrimaryKeyMapper {

    OrderPrimaryKey asOrderPrimaryKey(OrderIdPrimaryKey orderIdPrimaryKey);

    OrderIdPrimaryKey asOrderIdPrimaryKey(OrderPrimaryKey orderPrimaryKey);
}
