package com.project.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@TestConfiguration
@EnableFeignClients(basePackages = {"com.project.client"})
public class FeignConfig {
}
