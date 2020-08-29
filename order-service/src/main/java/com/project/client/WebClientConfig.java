package com.project.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "consumer")
@Getter
@Setter
public class WebClientConfig {

    private String paymentService;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(paymentService)
                .build();
    }
}
