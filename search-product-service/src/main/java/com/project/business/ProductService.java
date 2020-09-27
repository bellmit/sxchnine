package com.project.business;

import com.project.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
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
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.queryStringQuery("*" + query + "*")
                        .analyzeWildcard(true)
                        .field("name")
                        .field("brand")
                        .field("category"));


        QueryBuilder matchName = QueryBuilders.matchPhraseQuery("name", query);
        QueryBuilder matchBrand = QueryBuilders.fuzzyQuery("brand", query);
        QueryBuilder matchCategory = QueryBuilders.matchPhrasePrefixQuery("category", query);
        QueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(matchName)
                .should(matchBrand)
                .should(matchCategory);

        NativeSearchQueryBuilder nativeSearchQuery = new NativeSearchQueryBuilder();
        nativeSearchQuery.withQuery(boolQueryBuilder);

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

