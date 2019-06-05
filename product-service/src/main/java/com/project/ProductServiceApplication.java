package com.project;

import com.project.business.ProductService;
import com.project.configuration.KafkaProducerConfig;
import com.project.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2
@Import(value = KafkaProducerConfig.class)
public class ProductServiceApplication {
    static String[] args;
    @Autowired
    ProductService productService;

    public static void main(String[] args){
        //ProductServiceApplication.args = args;
        SpringApplication.run(ProductServiceApplication.class, args);
    }

/*    @PostConstruct
    public void ini(){
        productService.getAllProducts().stream().forEach(p -> productService.deleteProduct(p));
        for (int i=0; i<=10000; i++){
            Product product = new Product();
            product.setId(String.valueOf(i));
            product.setName("Product " + i);

            productService.save(product);

        }
    }*/
}
