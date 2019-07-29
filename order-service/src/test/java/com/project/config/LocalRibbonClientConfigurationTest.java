package com.project.config;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@RibbonClient("payment-service")
public class LocalRibbonClientConfigurationTest {

    @Bean
    public ServerList getServerList(){
        return new StaticServerList(new Server("localhost", 9091));
    }
}
