package com.project.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@ConfigurationProperties(prefix = "elasticsearch")
@RefreshScope
@Getter
@Setter
public class ElasticsearchProperties {

    private String index;
    private String server;

}
