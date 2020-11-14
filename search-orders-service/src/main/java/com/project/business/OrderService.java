package com.project.business;

import com.project.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final ReactiveElasticsearchOperations reactiveElasticsearchOperations;

    public Mono<Void> indexOrder(Order order) {
        return reactiveElasticsearchOperations.save(order)
                .doOnError(error -> log.error("error occurred during saving order", error))
                .then();
    }

    public Flux<Order> searchOrders(String user,
                                    String orderStatus,
                                    String orderTimeFrom,
                                    String orderTimeTo,
                                    String shippingTimeFrom,
                                    String shippingTimeTo){

        BoolQueryBuilder query = QueryBuilders.boolQuery();

        if (hasText(user)){
            query.must(QueryBuilders.matchQuery("orderKey.userEmail", user));
        }

        if (hasText(orderStatus)){
            //query.must(QueryBuilders.termQuery("orderStatus", orderStatus));
            query.filter(QueryBuilders.termQuery("orderStatus", orderStatus));
        }

        if (hasText(orderTimeFrom) && !hasText(orderTimeTo)){
            query.filter(QueryBuilders.rangeQuery("orderKey.orderTime").gte(orderTimeFrom));
        }

        if (!hasText(orderTimeFrom) && hasText(orderTimeTo)){
            query.filter(QueryBuilders.rangeQuery("orderKey.orderTime").lte(orderTimeTo));
        }

        if (hasText(orderTimeFrom) && hasText(orderTimeTo)){
            query.filter(QueryBuilders.rangeQuery("orderKey.orderTime")
                    .gte(orderTimeFrom)
                    .lte(orderTimeTo));
        }

        if (hasText(shippingTimeFrom) && !hasText(shippingTimeTo)){
            query.filter(QueryBuilders.rangeQuery("shippingTime").gte(shippingTimeFrom));
        }

        if (!hasText(shippingTimeFrom) && hasText(shippingTimeTo)){
            query.filter(QueryBuilders.rangeQuery("shippingTime").lte(shippingTimeTo));
        }

        if (hasText(shippingTimeFrom) && hasText(shippingTimeTo)){
            query.filter(QueryBuilders.rangeQuery("shippingTime")
                    .gte(shippingTimeFrom)
                    .lte(shippingTimeTo));
        }

        return reactiveElasticsearchOperations
                .search(new NativeSearchQuery(query), Order.class)
                .map(SearchHit::getContent)
                .doOnError(error -> log.error("cannot request ES with this request {}", query.toString(), error))
                .onErrorReturn(new Order());
    }

}
