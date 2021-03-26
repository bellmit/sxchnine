package com.project.configuration;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.project.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = ProductRepository.class)
@EnableMongoAuditing
@RequiredArgsConstructor
public class ReactiveMongoConfig extends AbstractReactiveMongoConfiguration {

    private final MongoProperties mongoProperties;

    @Override
    protected String getDatabaseName() {
        return mongoProperties.getDatabase();
    }

    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create(mongoProperties.getUri());
    }


}
