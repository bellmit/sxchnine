package com.project.configuration;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
    public Client client(){
        TransportClient transportClient = null;
        try {
            Settings settings = Settings.builder()
                    .put("client.transport.sniff", true)
                    .put("cluster.name", clusterName).build();
            transportClient = new PreBuiltTransportClient(settings);
            transportClient.addTransportAddresses(new TransportAddress(InetAddress.getByName(server), port));
        } catch (UnknownHostException e) {
            log.error("Exception occured on transport client", e);
        }
        return transportClient;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemple(){
        return new ElasticsearchTemplate(client());
    }
}
