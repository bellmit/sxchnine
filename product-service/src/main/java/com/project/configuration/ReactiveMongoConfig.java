package com.project.configuration;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = ProductRepository.class)
public class ReactiveMongoConfig extends AbstractReactiveMongoConfiguration {

    private static final String DB_NAME = "products";

    @Autowired
    private MongoProperties mongoProperties;


    @Bean
    public MongoClient reactiveMongoClient() {
        return MongoClients.create("mongodb://"+mongoProperties.getHost()+":"+mongoProperties.getPort());
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate(){
        return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
    }

    @Override
    protected String getDatabaseName() {
        return DB_NAME;
    }

}
