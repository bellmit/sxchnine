package com.project.utils;

import com.project.model.Order;
import com.project.model.Product;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;

public class PaymentUtil {

    public static BigDecimal sumTotal(Order order) {
        if (order != null && !CollectionUtils.isEmpty(order.getProducts())) {
            return order.getProducts()
                    .stream()
                    .filter(p -> p != null && p.getUnitPrice() != null)
                    .map(Product::getUnitPrice)
                    .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }
}
