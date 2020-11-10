package com.project.mapper;

import com.project.model.Order;
import com.project.model.OrderId;
import com.project.model.OrderStatus;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ofPattern;

@Mapper(uses = {
        OrderKeyMapper.class,
        OrderStatusKeyMapper.class
})
public abstract class OrderMapper {

    @Mapping(target = "orderKey", source = "orderIdKey")
    public abstract Order asOrder(OrderId orderId);

    @Mapping(target = "orderKey", source = "orderStatusKey")
    public abstract Order asOrderByOrderStatus(OrderStatus orderStatus);

    @Mapping(target = "orderIdKey", source = "orderKey")
    public abstract OrderId asOrderId(Order order);


    @Mapping(target = "orderIdKey", source = "orderStatusKey")
    public abstract OrderId asOrderIdByOrderStatus(OrderStatus orderStatus);

    @Mapping(target = "orderStatusKey", source = "orderKey")
    public abstract OrderStatus asOrderStatusByOrder(Order order);

    @AfterMapping
    public void setBucketValue(@MappingTarget OrderStatus orderStatus, Order order){
        orderStatus.getOrderStatusKey().setBucket(now().format(ofPattern("yyyyMM")));
    }

    @Mapping(target = "orderStatusKey", source = "orderIdKey")
    public abstract OrderStatus asOrderStatusByOrderId(OrderId orderId);
}
