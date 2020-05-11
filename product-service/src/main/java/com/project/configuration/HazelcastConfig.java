package com.project.configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class HazelcastConfig {

    @Bean
    public HazelcastInstance hazelcastInstance(){
        Config config = new Config();
        config.setInstanceName("products-cache");
        MapConfig productsCache = new MapConfig();
        productsCache.setTimeToLiveSeconds(2000);
        productsCache.setEvictionPolicy(EvictionPolicy.LRU);
        config.getMapConfigs().put("productsCache", productsCache);
        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    public CacheManager cacheManager(){
        return new HazelcastCacheManager(hazelcastInstance());
    }



}
