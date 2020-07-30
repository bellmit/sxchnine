package com.project.business;


import com.project.model.Product;
import com.project.model.SizeQte;
import com.project.repository.ProductRepository;
import com.project.util.FallbackProductsSource;
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
@Slf4j
public class ProductService {

    private ProductRepository productRepository;

    private KafkaProducer kafkaProducer;


    public ProductService(ProductRepository productRepository, KafkaProducer kafkaProducer) {
        this.productRepository = productRepository;
        this.kafkaProducer = kafkaProducer;
    }

    //@Cacheable(value = "productsCache", key = "#id", unless = "#result==null")
    public Mono<Product> getProductById(long id){
        log.info("Get Product {}", id);
        return productRepository.findById(id)
                .doOnError(error -> log.error("error occurred during getting product by id", error))
                .onErrorReturn(new Product());
    }

    public Flux<Product> getProductByIds(List<String> ids){
        log.info("Get Products by ids {}", ids);
        return productRepository.findProductsByIdIn(ids);
    }

    //@Cacheable(value = "productsCache", key = "#name", unless = "#result==null")
    public Mono<Product> getProductByName(String name){
        log.info("Get Product {}", name);
        return productRepository.findProductByName(name)
                .retryWhen(Retry.backoff(2, Duration.ofMillis(200)))
                .timeout(Duration.ofSeconds(2))
                .doOnError(error -> log.error("error occurred during getting product by name", error))
                .onErrorReturn(new Product());
    }

    //@Cacheable("productsCache")
    public Flux<Product> getAllProducts(){
        log.info("Get all products");
        return productRepository.findAll()
                .doOnError(error -> log.error("error occurred during getting all products", error));

    }

    //@Cacheable("productsCache")
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


    //@CachePut(value = "productsCache", key = "#product.id")
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

    //@CacheEvict(value = "productsCache", key = "#id")
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
