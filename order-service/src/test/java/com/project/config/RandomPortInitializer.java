package com.project.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.SocketUtils;

public class RandomPortInitializer implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        int port = SocketUtils.findAvailableTcpPort();
        //applicationContext.
    }
}
