package com.project.repository;

import com.project.model.IndexedOrder;
import com.project.model.Order;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends ElasticsearchCrudRepository<IndexedOrder, String> {
}
