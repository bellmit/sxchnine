package utils;

import com.project.model.Product;

import java.math.BigDecimal;
import java.util.HashMap;

public class TestObjectCreator {

    public static Product createProduct(){
        Product product = new Product();
        product.setId(1L);
        product.setName("p1");
        product.setPrice(BigDecimal.valueOf(1.0));
        product.setAvailability(new HashMap<>());
        return product;
    }
}
