package com.project.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
public class ProductConsumer {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    public ProductConsumer(ProductService productService, ObjectMapper objectMapper) {
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.groupId}")
    public void consumeProduct(String product, Acknowledgment ack) throws JsonProcessingException {
        log.info("Product Received: {}", product);
        Product productToSave = objectMapper.readValue(product, Product.class);
        productService.save(productToSave)
                .doOnSuccess(p -> log.info("product {} consumed", productToSave.getId()))
                .doOnError(error -> log.error("error happened during consuming product " + productToSave.getId(), error))
                .subscribe();
        ack.acknowledge();
    }
}
