package com.project.repository;

import com.project.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, String> {

    Optional<Product> findProductByName(String name);

    Page<Product> findAllBySex(Pageable page, char sex);
}
