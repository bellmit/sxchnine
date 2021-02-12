package com.project.business;

import com.project.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductService {

    private final ReactiveElasticsearchOperations reactiveElasticsearchOperations;

    public ProductService(ReactiveElasticsearchOperations reactiveElasticsearchOperations) {
        this.reactiveElasticsearchOperations = reactiveElasticsearchOperations;
    }

    public Flux<Product> getProductsByQuery(String query) {
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders
                .multiMatchQuery(query)
                .field("name")
                .field("brand", 5.0F)
                .field("category")
                .field("tags")
                .type(MultiMatchQueryBuilder.Type.CROSS_FIELDS);

        NativeSearchQueryBuilder nativeSearchQuery = new NativeSearchQueryBuilder();
        nativeSearchQuery.withQuery(multiMatchQueryBuilder);

        return reactiveElasticsearchOperations
                .search(nativeSearchQuery.build(), Product.class, Product.class)
                .map(SearchHit::getContent)
                .doOnError(error -> log.error("error occurred during search", error));
    }

    public Flux<Product> getProductsByAdvancedFiltering(String gender, String brand, String category, String size) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        if (StringUtils.hasText(gender)) {
            queryBuilder.must(QueryBuilders.matchQuery("sex", gender));
        }

        if (StringUtils.hasText(brand)) {
            queryBuilder.must(QueryBuilders.matchQuery("brand", brand));
        }

        if (StringUtils.hasText(category)) {
            queryBuilder.must(QueryBuilders.matchQuery("category", category));
        }

        if (StringUtils.hasText(size)) {
            queryBuilder.must(QueryBuilders.matchQuery("size", size));
        }

        NativeSearchQueryBuilder nativeQuery = new NativeSearchQueryBuilder();
        nativeQuery.withQuery(queryBuilder);

        return reactiveElasticsearchOperations
                .search(nativeQuery.build(), Product.class, Product.class)
                .map(SearchHit::getContent)
                .doOnError(error -> log.error("error occurred during advanced search", error));
    }


    public Mono<Void> save(Product product) {
        return reactiveElasticsearchOperations.save(product)
                .doOnError(error -> log.error("error occurred during saving", error))
                .then();
    }

    public Mono<Void> deleteById(String id){
        return reactiveElasticsearchOperations.delete(id, Product.class)
                .doOnError(error -> log.error("error occurred during delete product by id {}", id, error))
                .then();
    }
}

