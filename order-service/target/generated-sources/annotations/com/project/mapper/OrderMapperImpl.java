package com.project.mapper;

import com.project.model.Order;
import com.project.model.OrderId;
import com.project.model.Product;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-06-02T23:06:59-0400",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Autowired
    private OrderPrimaryKeyMapper orderPrimaryKeyMapper;

    @Override
    public Order asOrder(OrderId orderId) {
        if ( orderId == null ) {
            return null;
        }

        Order order = new Order();

        order.setOrderPrimaryKey( orderPrimaryKeyMapper.asOrderPrimaryKey( orderId.getOrderIdPrimaryKey() ) );
        List<Product> list = orderId.getProducts();
        if ( list != null ) {
            order.setProducts( new ArrayList<Product>( list ) );
        }
        order.setProductBrand( orderId.getProductBrand() );
        order.setProductName( orderId.getProductName() );
        order.setTotal( orderId.getTotal() );
        order.setPaymentInfo( orderId.getPaymentInfo() );
        order.setUserAddress( orderId.getUserAddress() );
        order.setOrderStatus( orderId.getOrderStatus() );
        order.setPaymentStatus( orderId.getPaymentStatus() );
        order.setPaymentTime( orderId.getPaymentTime() );
        order.setShippingStatus( orderId.getShippingStatus() );

        return order;
    }

    @Override
    public OrderId asOrderId(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderId orderId = new OrderId();

        orderId.setOrderIdPrimaryKey( orderPrimaryKeyMapper.asOrderIdPrimaryKey( order.getOrderPrimaryKey() ) );
        List<Product> list = order.getProducts();
        if ( list != null ) {
            orderId.setProducts( new ArrayList<Product>( list ) );
        }
        orderId.setProductBrand( order.getProductBrand() );
        orderId.setProductName( order.getProductName() );
        orderId.setTotal( order.getTotal() );
        orderId.setPaymentInfo( order.getPaymentInfo() );
        orderId.setUserAddress( order.getUserAddress() );
        orderId.setOrderStatus( order.getOrderStatus() );
        orderId.setPaymentStatus( order.getPaymentStatus() );
        orderId.setPaymentTime( order.getPaymentTime() );
        orderId.setShippingStatus( order.getShippingStatus() );

        return orderId;
    }
}
