package com.project.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    private final SubscriptionProperties subscriptionProperties;

    public WebClientConfiguration(SubscriptionProperties subscriptionProperties) {
        this.subscriptionProperties = subscriptionProperties;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(subscriptionProperties.getUrl())
                .build();
    }
}
