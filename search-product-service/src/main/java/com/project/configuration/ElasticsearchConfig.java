package com.project.configuration;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
@Slf4j
public class ElasticsearchConfig {

    @Value("${elasticsearch.name}")
    private String clusterName;

    @Value("${elasticsearch.server}")
    private String server;

    @Value("${elasticsearch.port}")
    private int port;

    @Bean
    public RestHighLevelClient elasticSearchClient(){
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(server+":"+port)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(elasticSearchClient());
    }

}
