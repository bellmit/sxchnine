package com.project.business;

import com.project.model.Product;
import com.project.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProductsByQuery(String query) {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.queryStringQuery("*" + query + "*")
                        .analyzeWildcard(true)
                        .field("name")
                        .field("brand")
                        .field("category"));


        QueryBuilder matchName = QueryBuilders.fuzzyQuery("name", query);
        QueryBuilder matchBrand = QueryBuilders.fuzzyQuery("brand", query);
        QueryBuilder matchCategory = QueryBuilders.matchPhrasePrefixQuery("category", query);
        QueryBuilder queryBuilder1 = QueryBuilders.boolQuery()
                .should(matchName)
                .should(matchBrand)
                .should(matchCategory);

        return productRepository.search(queryBuilder1);
    }

    public List<Product> getProductsByAdvancedFiltering(String gender, String brand, String category, String size) {
        log.info(" ********************* search product by advanced filter " + Thread.currentThread().getName());
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        if (StringUtils.isNotBlank(gender)) {
            queryBuilder.must(QueryBuilders.matchQuery("sex", gender));
        }

        if (StringUtils.isNotBlank(brand)) {
            queryBuilder.must(QueryBuilders.matchQuery("brand", brand));
        }

        if (StringUtils.isNotBlank(category)) {
            queryBuilder.must(QueryBuilders.matchQuery("category", category));
        }

        if (StringUtils.isNotBlank(size)) {
            queryBuilder.must(QueryBuilders.matchQuery("size", size));
        }

        return productRepository.search(queryBuilder);
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void saveProducts(List<Product> products) {
        productRepository.saveAll(products);
    }

    public void delete(Product product){
        productRepository.delete(product);
    }

    public void deleteById(String id){
        productRepository.deleteById(id);
    }

    public void deleteProducts(List<Product> products){
        productRepository.deleteAll(products);
    }
}

