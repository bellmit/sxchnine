package com.project.config;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class LocalRibbonClientConfigurationTest {

    @Value("${port.ribbon}")
    private int port;

    @Bean
    public ServerList getServerList(){
        return new StaticServerList(new Server("localhost", port));
    }
}
