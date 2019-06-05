package utils;

import com.project.model.Product;

import java.math.BigDecimal;

public class TestObjectCreator {

    public static Product createProduct(){
        Product product = new Product();
        product.setId("1");
        product.setName("p1");
        product.setPrice(BigDecimal.valueOf(1.0));
        return product;
    }
}
