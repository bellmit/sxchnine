package com.project.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConfigurationProperties(prefix = "stripe")
@Getter
@Setter
public class WebClientConfiguration {

    private String url;
    private String publicKey;

    @Bean
    public WebClient webClient(){
        return WebClient
                .builder()
                .baseUrl(url)
                .defaultHeader("Authorization", "Bearer " + publicKey)
                .build();
    }
}
