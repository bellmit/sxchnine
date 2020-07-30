package com.project.apigateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;

@EnableWebFluxSecurity
@Configuration
public class GatewayConfig {

    @Bean
    SecurityWebFilterChain configure(ServerHttpSecurity http){
        http.csrf().disable()
                .authorizeExchange()
                .pathMatchers("/index", "/oauth2/authorization/**, /authorize/**, /login/**, api-gateway/**").permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .oauth2ResourceServer().jwt();
        
        http.headers().frameOptions().mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN);

        return http.build();
    }

}
