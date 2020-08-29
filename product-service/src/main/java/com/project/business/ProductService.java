package com.project.business;


import com.project.model.Product;
import com.project.model.SizeQte;
import com.project.repository.ProductRepository;
import com.project.util.FallbackProductsSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    private final KafkaProducer kafkaProducer;

    public Mono<Product> getProductById(Long id){
        log.info("Get Product {}", id);
        return productRepository.findProductById(id)
                .doOnError(error -> log.error("error occurred during getting product by id", error))
                .onErrorReturn(new Product());
    }

    public Flux<Product> getProductByIds(List<Long> ids){
        log.info("Get Products by ids {}", ids);
        return productRepository.findProductsByIdIn(ids);
    }

    public Mono<Product> getProductByName(String name){
        log.info("Get Product {}", name);
        return productRepository.findProductByName(name)
                .retryWhen(Retry.backoff(2, Duration.ofMillis(200)))
                .timeout(Duration.ofSeconds(2))
                .doOnError(error -> log.error("error occurred during getting product by name", error))
                .onErrorReturn(new Product());
    }

    public Flux<Product> getAllProducts(){
        return productRepository.findAll()
                .doOnError(error -> log.error("error occurred during getting all products", error));

    }

    public Flux<Product> getAllProductsBySex(int pageNo, int pageSize, char sex){
        log.info("Get all products by sex");
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return productRepository.findAllBySex(sex, paging)
                .retry()
                .retryWhen(Retry.backoff(2, Duration.ofMillis(500)))
                .timeout(Duration.ofSeconds(5))
                .doOnError(error -> log.error("error occurred during getting product by sex", error))
                .onErrorResume(p -> FallbackProductsSource.fallbackProducts(sex));
    }

    public Mono<Product> save(Product product){
        log.info("save product");
        return productRepository.save(product)
                .log()
                .flatMap(p -> kafkaProducer
                        .sendProduct(Mono.just(p))
                        .doOnError(error -> log.info("error happened when sending to Kafka {}", product, error)))
                .doOnError(error -> log.error("Error during saving", error));
    }

    public Mono<Void> saveProducts(Flux<Product> products){
        return productRepository.saveAll(products)
                .flatMap(p -> kafkaProducer
                        .sendProduct(Mono.just(p))
                        .doOnError(error -> log.info("error happened when sending to Kafka {}", p, error)))
                .doOnError(error -> log.error("Error during saving all products", error))
                .then();
    }

    public Mono<Void> deleteProductById(long id){
        log.debug("Product {}  delete ", id);
        return productRepository.deleteById(id);
    }

    private Product createMockProduct(){
        Set<String> size = new HashSet<>();
        size.add("S");
        size.add("M");
        size.add("L");

        Set<String> colors = new HashSet<>();
        colors.add("Black");
        colors.add("White");

        SizeQte sizeQteS = new SizeQte();
        sizeQteS.setSize('S');
        sizeQteS.setQte(2);

        SizeQte sizeQteM = new SizeQte();
        sizeQteM.setSize('M');
        sizeQteM.setQte(3);

        Set<SizeQte> sizeQtesBlack = new HashSet<>();
        sizeQtesBlack.add(sizeQteS);
        sizeQtesBlack.add(sizeQteM);

        Map<String, Set<SizeQte>> availablities = new HashMap<>();
        availablities.put("Black", sizeQtesBlack);
        availablities.put("White", sizeQtesBlack);

        return Product.builder()
                .id(RandomUtils.nextLong())
                .name("Sweat - Crew")
                .brand("Nike")
                .logo("https://www.festisite.com/static/partylogo/img/logos/nike.png")
                .sex('W')
                .category("Sweat")
                .price(BigDecimal.valueOf(80))
                .images(Collections.singleton("https://images.asos-media.com/products/nike-logo-crew-sweat-in-white-804340-100/7134528-3?$XXL$&wid=513&fit=constrain"))
                .size(size)
                .colors(colors)
                .availability(availablities)
                .build();

    }
}
