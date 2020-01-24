package com.project.repository;

import com.project.model.Product;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {

    List<Product> findAll();

    List<Product> search(QueryBuilder queryBuilder);
}
