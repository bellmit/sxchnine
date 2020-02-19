package com.project.service;

import com.project.client.ProductService;
import com.project.model.Order;
import com.project.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
    @RestClient
    private ProductService productService;

    public String manageStock(Order order){
        if (!order.getProducts().isEmpty()) {

            List<String> productsIds = order.getProducts()
                    .stream()
                    .map(Product::getProductId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            Map<String, com.project.client.model.Product> productsByIds = productService.getProductsByIds(productsIds)
                    .stream()
                    .collect(Collectors.toMap(com.project.client.model.Product::getId, Function.identity(), (p1, p2) -> p1));

            if (productsByIds.get(PRODUCT_FALLBACK.getValue()) != null){
                return PRODUCT_FALLBACK.getValue();
            }
            matchAvailability(productsByIds, order.getProducts());

            log.info("Products to update {}", productsByIds.toString());

            updateProducts(productsByIds);
        }
        return "";
    }

    private void matchAvailability(Map<String, com.project.client.model.Product> productsByIds, List<Product> orderedProduct){
        orderedProduct.forEach(p -> {
            if (productsByIds.get(p.getProductId()) != null){
                if (productsByIds.get(p.getProductId()).getAvailability().get(p.getProductColor()) != null){
                    productsByIds.get(p.getProductId()).getAvailability().get(p.getProductColor())
                            .stream().
                            filter(sqt -> String.valueOf(sqt.getSize()).equals(p.getProductSize()))
                            .forEach(sqt -> sqt.setQte(sqt.getQte() - 1));
                }
            }
        });
    }

    private void updateProducts(Map<String, com.project.client.model.Product> productsByIds){
        productService.createOrUpdateProducts(productsByIds.values().stream().collect(Collectors.toList()));
    }

}
