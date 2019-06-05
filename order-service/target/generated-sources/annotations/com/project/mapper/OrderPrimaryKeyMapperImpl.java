package com.project.mapper;

import com.project.model.OrderIdPrimaryKey;
import com.project.model.OrderPrimaryKey;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-06-02T23:06:59-0400",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
@Component
public class OrderPrimaryKeyMapperImpl implements OrderPrimaryKeyMapper {

    @Override
    public OrderPrimaryKey asOrderPrimaryKey(OrderIdPrimaryKey orderIdPrimaryKey) {
        if ( orderIdPrimaryKey == null ) {
            return null;
        }

        OrderPrimaryKey orderPrimaryKey = new OrderPrimaryKey();

        orderPrimaryKey.setUserEmail( orderIdPrimaryKey.getUserEmail() );
        orderPrimaryKey.setOrderId( orderIdPrimaryKey.getOrderId() );
        orderPrimaryKey.setOrderTime( orderIdPrimaryKey.getOrderTime() );
        orderPrimaryKey.setShippingTime( orderIdPrimaryKey.getShippingTime() );

        return orderPrimaryKey;
    }

    @Override
    public OrderIdPrimaryKey asOrderIdPrimaryKey(OrderPrimaryKey orderPrimaryKey) {
        if ( orderPrimaryKey == null ) {
            return null;
        }

        OrderIdPrimaryKey orderIdPrimaryKey = new OrderIdPrimaryKey();

        orderIdPrimaryKey.setOrderId( orderPrimaryKey.getOrderId() );
        orderIdPrimaryKey.setUserEmail( orderPrimaryKey.getUserEmail() );
        orderIdPrimaryKey.setOrderTime( orderPrimaryKey.getOrderTime() );
        orderIdPrimaryKey.setShippingTime( orderPrimaryKey.getShippingTime() );

        return orderIdPrimaryKey;
    }
}
