package com.project.configuration;

import com.project.repository.ProductRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = ProductRepository.class)
public class ReactiveMongoConfig{

}
