package com.project.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfiguration {

    private final SubscriptionProperties subscriptionProperties;

    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl(subscriptionProperties.getUrl())
                .build();
    }
}
