package com.project.util;

import com.project.model.Men;
import com.project.model.Women;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@ConfigurationProperties(prefix = "fallback.products")
@RefreshScope
@Getter
@Setter
public class FallbackProductsConfiguration {

    private Men men;
    private Women women;
}
