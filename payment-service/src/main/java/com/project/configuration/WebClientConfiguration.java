package com.project.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfiguration {

    private final StConfigurationProperties vendorProperties;
    private final OrderConfigurationProperties orderProperties;

    @Bean
    public WebClient vendorWebClient() {
        return WebClient
                .builder()
                .baseUrl(vendorProperties.getUrl())
                .defaultHeader("Authorization", "Bearer " + vendorProperties.getPublicKey())
                .build();
    }

    @Bean
    public WebClient orderWebClient() {
        return WebClient.builder()
                .baseUrl(orderProperties.getUrl())
                .build();
    }
}
