package com.project.mapper;

import com.project.model.IndexedOrder;
import com.project.model.Order;
import com.project.model.Product;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = {UuidMapper.class})
public abstract class OrderMapper {

    @Mapping(target = "userEmail", source = "order.orderPrimaryKey.userEmail")
    @Mapping(target = "orderId", source = "order.orderPrimaryKey.orderId")
    @Mapping(target = "orderTime", source = "order.orderPrimaryKey.orderTime")
    @Mapping(target = "shippingTime", source = "order.orderPrimaryKey.shippingTime")
    @Mapping(target = "paymentTime", source = "order.paymentTime")
    @Mapping(target = "productName", source = "product.productName")
    @Mapping(target = "productBrand", source = "product.productBrand")
    public abstract IndexedOrder asIndexedOrder(Order order, Product product);

    @AfterMapping
    public void populateId(@MappingTarget IndexedOrder indexedOrder, Order order, Product product){
        indexedOrder.setId(indexedOrder.getOrderId() + indexedOrder.getProductId());
    }

    public List<IndexedOrder> asIndexedOrders(Order order){
        return order.getProducts()
                .stream()
                .map(p -> asIndexedOrder(order, p))
                .collect(Collectors.toList());
    }

}
