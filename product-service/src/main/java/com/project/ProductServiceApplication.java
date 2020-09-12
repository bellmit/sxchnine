package com.project;

import com.project.util.FallbackProductsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FallbackProductsConfiguration.class)
public class ProductServiceApplication {

    public static void main(String[] args){
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}
