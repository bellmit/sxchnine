package com.project.configuration;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.project.repository.ProductRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = ProductRepository.class)
@EnableMongoAuditing
public class ReactiveMongoConfig extends AbstractReactiveMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "products";
    }

    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create();
    }


}
