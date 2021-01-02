package com.project.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "users.subscriptions")
@Getter
@Setter
@AllArgsConstructor
public class SubscriptionProperties {

    private String url;
}
