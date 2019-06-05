package utils;

import com.project.model.Product;

public class TestObjectCreator {

    public static Product createProduct(){
        Product product = new Product();
        product.setId("1");
        product.setName("product");
        product.setPrice(20.00);
        return product;
    }
}
