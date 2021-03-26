package com.project.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.project.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.List;

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = OrderRepository.class)
@EnableReactiveMongoAuditing
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

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(List.of(new ZoneDateTimeReadConverter(), new ZoneDateTimeWriteConverter()));
    }

}
