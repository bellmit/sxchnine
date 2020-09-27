package com.project.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.client.model.Product;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static com.project.utils.ProductCode.PRODUCT_FALLBACK;

@ApplicationScoped
@Slf4j
public class ProductServiceClient {

    @Inject
    Vertx vertx;

    @ConfigProperty(name = "product.service.host")
    String host;

    @ConfigProperty(name = "product.service.port")
    int port;

    private WebClient webClient;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @PostConstruct
    public void setup(){
        this.webClient = WebClient
                .create(vertx, new WebClientOptions().setDefaultHost(host)
                        .setDefaultPort(port)
                        .setTrustAll(true));
    }

    public Uni<List<Product>> getProductsByIds(List<String> ids){
        log.info("getting product from product service - {} ...", String.join(",", ids));
        return webClient.get("/ids?ids=" + String.join(",", ids))
                .putHeader("Content-Type", "application/json")
                .send()
                .onItem()
                .transform(r -> {
                    try {
                        return objectMapper.readValue(r.bodyAsString(), new TypeReference<List<Product>>(){});
                    } catch (JsonProcessingException e) {
                        log.error("Error while deserializing product get response", e);
                        return Collections.singletonList(Product.builder().id(PRODUCT_FALLBACK.getValue()).build());
                    }
                })
                .ifNoItem()
                .after(Duration.ofSeconds(5))
                .recoverWithItem(Collections.singletonList(Product.builder().id(PRODUCT_FALLBACK.getValue()).build()))
                .onFailure()
                .recoverWithItem(t -> {
                    log.error("error happened while calling get products for cause {}", t.getMessage());
                    return Collections.singletonList(Product.builder().id(PRODUCT_FALLBACK.getValue()).build());
                });
    }

    public Uni<String> createOrUpdateProducts(List<Product> products){
        log.info("update products ...");
        return webClient.post("/bulk")
                .putHeader("Content-Type", "application/json")
                .sendJson(products)
                .onFailure()
                .transform(response -> {
                    log.info("Error happened while calling bulk product", response);
                    return response;
                })
                .onItem()
                .transform(response -> {
                    if (response.statusCode() == 200){
                        return "SUCCESS";
                    } else {
                        return "ERROR";
                    }
                });
    }

}
