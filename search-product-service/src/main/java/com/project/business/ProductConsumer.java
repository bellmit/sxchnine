package com.project.business;

import com.netflix.discovery.converters.Auto;
import com.project.model.Product;
import com.project.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductConsumer {

    @Autowired
    private ProductRepository productRepository;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.groupId}")
    public void consumeProduct(Product product, Acknowledgment ack){
        log.info("*************************************");
        log.info("**** Received: {}", product);
        log.info("*************************************");
        productRepository.save(product);
        ack.acknowledge();
    }
}
