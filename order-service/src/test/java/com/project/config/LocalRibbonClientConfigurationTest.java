package com.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class LocalRibbonClientConfigurationTest {

    @Value("${port.ribbon}")
    private int port;

}
