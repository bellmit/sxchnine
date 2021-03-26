package com.project.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaSender kafkaSender(){
        return KafkaSender.create(senderOptions());
    }

    public SenderOptions<String, String> senderOptions(){
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configs.put(ProducerConfig.CLIENT_ID_CONFIG, "products");
        configs.put(ProducerConfig.ACKS_CONFIG, "all");
        configs.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        configs.put(ProducerConfig.RETRIES_CONFIG, 3);
        configs.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 1_0000);
        configs.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 1_0000);
        configs.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1_000);
        configs.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 1_0000);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return SenderOptions.create(configs);

    }


}

