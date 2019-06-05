package com.project.mapper;

import com.project.model.IndexedOrder;
import com.project.model.Order;
import com.project.model.OrderPrimaryKey;
import com.project.model.Product;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-05-19T18:02:57-0400",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl extends OrderMapper {

    @Autowired
    private UuidMapper uuidMapper;

    @Override
    public IndexedOrder asIndexedOrder(Order order, Product product) {
        if ( order == null && product == null ) {
            return null;
        }

        IndexedOrder indexedOrder = new IndexedOrder();

        if ( order != null ) {
            LocalDateTime orderTime = orderOrderPrimaryKeyOrderTime( order );
            if ( orderTime != null ) {
                indexedOrder.setOrderTime( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( orderTime ) );
            }
            indexedOrder.setOrderId( uuidMapper.asString( orderOrderPrimaryKeyOrderId( order ) ) );
            indexedOrder.setUserEmail( orderOrderPrimaryKeyUserEmail( order ) );
            LocalDateTime shippingTime = orderOrderPrimaryKeyShippingTime( order );
            if ( shippingTime != null ) {
                indexedOrder.setShippingTime( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( shippingTime ) );
            }
            if ( order.getPaymentTime() != null ) {
                indexedOrder.setPaymentTime( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( order.getPaymentTime() ) );
            }
            indexedOrder.setUserAddress( order.getUserAddress() );
            indexedOrder.setOrderStatus( order.getOrderStatus() );
            indexedOrder.setPaymentStatus( order.getPaymentStatus() );
            indexedOrder.setShippingStatus( order.getShippingStatus() );
        }
        if ( product != null ) {
            indexedOrder.setProductBrand( product.getProductBrand() );
            indexedOrder.setProductName( product.getProductName() );
            indexedOrder.setProductId( product.getProductId() );
            indexedOrder.setProductSize( product.getProductSize() );
            indexedOrder.setProductColor( product.getProductColor() );
            indexedOrder.setProductQte( product.getProductQte() );
            indexedOrder.setUnitPrice( product.getUnitPrice() );
            indexedOrder.setStore( product.getStore() );
        }

        populateId( indexedOrder, order, product );

        return indexedOrder;
    }

    private LocalDateTime orderOrderPrimaryKeyOrderTime(Order order) {
        if ( order == null ) {
            return null;
        }
        OrderPrimaryKey orderPrimaryKey = order.getOrderPrimaryKey();
        if ( orderPrimaryKey == null ) {
            return null;
        }
        LocalDateTime orderTime = orderPrimaryKey.getOrderTime();
        if ( orderTime == null ) {
            return null;
        }
        return orderTime;
    }

    private UUID orderOrderPrimaryKeyOrderId(Order order) {
        if ( order == null ) {
            return null;
        }
        OrderPrimaryKey orderPrimaryKey = order.getOrderPrimaryKey();
        if ( orderPrimaryKey == null ) {
            return null;
        }
        UUID orderId = orderPrimaryKey.getOrderId();
        if ( orderId == null ) {
            return null;
        }
        return orderId;
    }

    private String orderOrderPrimaryKeyUserEmail(Order order) {
        if ( order == null ) {
            return null;
        }
        OrderPrimaryKey orderPrimaryKey = order.getOrderPrimaryKey();
        if ( orderPrimaryKey == null ) {
            return null;
        }
        String userEmail = orderPrimaryKey.getUserEmail();
        if ( userEmail == null ) {
            return null;
        }
        return userEmail;
    }

    private LocalDateTime orderOrderPrimaryKeyShippingTime(Order order) {
        if ( order == null ) {
            return null;
        }
        OrderPrimaryKey orderPrimaryKey = order.getOrderPrimaryKey();
        if ( orderPrimaryKey == null ) {
            return null;
        }
        LocalDateTime shippingTime = orderPrimaryKey.getShippingTime();
        if ( shippingTime == null ) {
            return null;
        }
        return shippingTime;
    }
}
