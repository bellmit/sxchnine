package com.project;

import com.project.configuration.ElasticsearchProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class SearchProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchProductServiceApplication.class, args);
    }
}
