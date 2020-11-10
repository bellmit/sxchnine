package com.project.repository;

import com.project.model.Order;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends ElasticsearchCrudRepository<Order, String> {
}
