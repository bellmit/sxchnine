package com.project.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Bean
    public ErrorClientDecoder errorClientDecoder(){
        return new ErrorClientDecoder();
    }
}
