package com.project.repository;

import com.project.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, Long> {

    Mono<Product> findProductById(Long id);

    Mono<Product> findProductByName(String name);

    Flux<Product> findAllBySex(char sex, Pageable page);

    Flux<Product> findProductsByIdIn(List<Long> ids);

    Flux<Product> findProductsByIdAndNameAndBrandAndSex(Long id, String name, String brand, String sex);

    Flux<Product> findProductsByNameAndBrandAndSex(String name, String brand, String sex);

    Flux<Product> findProductsByBrandAndSex(String brand, String sex);

    Flux<Product> findProductsByBrand(String brand);

    Flux<Product> findProductsByNameAndBrand(String name, String brand);
}
