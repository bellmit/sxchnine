package com.project.service;

import com.project.client.ProductServiceClient;
import com.project.model.Order;
import com.project.model.ProductOrder;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.project.utils.ProductCode.PRODUCT_FALLBACK;

@ApplicationScoped
@Slf4j
public class StockService {

    @Inject
    ProductServiceClient productServiceClient;

    public Uni<String> manageStock(Order order){
        log.info("start managing stock ...");
        if (!order.getProductOrders().isEmpty()) {
            List<String> productsIds = order.getProductOrders()
                    .stream()
                    .map(ProductOrder::getProductId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            return productServiceClient
                    .getProductsByIds(productsIds)
                    .onItem()
                    .transform(products -> products
                            .stream()
                            .collect(Collectors
                                    .toMap(com.project.client.model.Product::getId, Function.identity())))
                    .onItem()
                    .transform(result -> matchAvailability(result, order.getProductOrders()))
                    .onItem()
                    .transformToUni(this::updateProducts);
        }
        return Uni.createFrom().item("ERROR");
    }

    private Map<Long, com.project.client.model.Product> matchAvailability(Map<Long, com.project.client.model.Product> productsByIds, List<ProductOrder> orderedProduct){
        log.info("start matching availability ... ");
        if (productsByIds.get(PRODUCT_FALLBACK.getValue()) == null) {
            orderedProduct.forEach(p -> {
                if (productsByIds.get(p.getProductId()) != null) {
                    if (productsByIds.get(p.getProductId()).getAvailability().get(p.getProductColor()) != null) {
                        productsByIds.get(p.getProductId()).getAvailability().get(p.getProductColor())
                                .stream()
                                .filter(sqt -> String.valueOf(sqt.getSize()).equals(p.getProductSize()))
                                .forEach(sqt -> sqt.setQte(sqt.getQte() - 1));
                    }
                }
            });
            log.info("finish matching availability - found 1");
        }
        log.info("finish matching availability ...");
        return productsByIds;
    }

    private Uni<String> updateProducts(Map<Long, com.project.client.model.Product> productsByIds){
        if (productsByIds.get(PRODUCT_FALLBACK.getValue()) == null){
            return productServiceClient.createOrUpdateProducts(new ArrayList<>(productsByIds.values()));
        } else {
            log.info("product fallback found - no update product call");
            return Uni.createFrom().item("ERROR");
        }
    }

}
