package com.project.business;


import com.project.model.Product;
import com.project.model.SizeQte;
import com.project.repository.ProductRepository;
import com.project.util.FallbackProductsSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.cloud.sleuth.instrument.web.WebFluxSleuthOperators.withSpanInScope;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    private final KafkaProducer kafkaProducer;

    public Mono<Product> getProductById(Long id) {
        return productRepository.findProductById(id)
                .doOnError(error -> log.error("error occurred during fetching product by id", error))
                .onErrorReturn(new Product());
    }

    public Flux<Product> getProductByIds(List<Long> ids) {
        return productRepository.findProductsByIdIn(ids)
                .doOnEach(withSpanInScope(SignalType.ON_COMPLETE, signal -> log.info("Fetch list of product IDs: {}", ids.toString())));

    }

    public Mono<Product> getProductByName(String name) {
        return productRepository.findProductByNameLike(name)
                .retryWhen(Retry.backoff(2, Duration.ofMillis(200)))
                .doOnError(error -> log.error("error occurred during fetching product by name", error))
                .onErrorReturn(new Product());
    }

    public Flux<Product> getAllProducts() {
        return productRepository.findAll()
                .doOnError(error -> log.error("error occurred during getting all products", error));

    }

    public Flux<Product> getAllProductsBySex(int pageNo, int pageSize, char sex) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return productRepository.findAllBySex(sex, paging)
                .retry()
                .retryWhen(Retry.backoff(2, Duration.ofMillis(500)))
                .doOnError(error -> log.error("error occurred during getting product by sex", error))
                .onErrorResume(p -> FallbackProductsSource.fallbackProducts(sex));
    }

    public Flux<Product> searchProducts(Long id, String name, String brand, String sex) {
        if (id != null) {
            return Flux.from(productRepository.findProductById(id));

        } else if (StringUtils.hasText(name)
                && StringUtils.hasText(brand)
                && StringUtils.hasText(sex)) {

            return productRepository.findProductsByNameAndBrandAndSex(name, brand, sex);

        } else if (StringUtils.hasText(name)
                && StringUtils.hasText(brand)) {

            return productRepository.findProductsByNameAndBrand(name, brand);

        } else if (StringUtils.hasText(brand)
                && StringUtils.hasText(sex)) {
            return productRepository.findProductsByBrandAndSex(brand, sex);

        } else if (StringUtils.hasText(brand)) {
            return productRepository.findProductsByBrandLike(brand);

        } else if (StringUtils.hasText(name)) {
            return Flux.from(productRepository.findProductByNameLike(name));
        } else {
            return productRepository.findProductsByIdAndNameAndBrandAndSex(id, name, brand, sex);
        }
    }

    public Mono<Product> save(Product product) {
        sumQteAndSetDate(product);
        return productRepository.save(product)
                .doOnEach(withSpanInScope(SignalType.ON_NEXT, signal -> log.info("Save product with ID: {}", product.getId())))
                .flatMap(p -> kafkaProducer
                        .sendProduct(Mono.just(p))
                        .doOnError(error -> log.info("error happened when sending to Kafka {}", product, error)))
                .doOnError(error -> log.error("Error during saving", error));
    }

    private void sumQteAndSetDate(Product product) {
        Integer totalQte = product.getAvailability().values()
                .stream()
                .flatMap(v -> v.stream().map(SizeQte::getQte))
                .reduce(0, Integer::sum);
        product.setQuantity(totalQte);
        product.setDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")));
    }

    public Mono<Void> saveProducts(List<Product> products) {
        return productRepository.saveAll(products)
                .doOnEach(withSpanInScope(SignalType.ON_NEXT, signal -> log.info("Bulk - Save list of products")))
                .flatMap(p -> kafkaProducer
                        .sendProduct(Mono.just(p))
                        .doOnError(error -> log.info("error happened when sending to Kafka {}", p, error)))
                .doOnError(error -> log.error("Error during saving all products", error))
                .then();
    }

    public Mono<Void> deleteProductById(long id) {
        return productRepository.deleteById(id)
                .doOnEach(withSpanInScope(SignalType.ON_COMPLETE, signal -> log.info("Delete product with ID: {}", id)));
    }

}
