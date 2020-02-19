package com.project.business;


import com.project.exception.ProductNotFoundException;
import com.project.model.Product;
import com.project.model.SizeQte;
import com.project.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


@Service
@Slf4j
public class ProductService {

    private ProductRepository productRepository;

    private KafkaProducer kafkaProducer;

    public ProductService(ProductRepository productRepository, KafkaProducer kafkaProducer) {
        this.productRepository = productRepository;
        this.kafkaProducer = kafkaProducer;
    }

    @Cacheable(value = "productsCache", key = "#id", unless = "#result==null")
    public Product getProductById(String id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found !"));
    }

    public List<Product> getProductByIds(List<String> ids){
        return productRepository.findProductsByIdIn(ids);
    }

    @Cacheable(value = "productsCache", key = "#name", unless = "#result==null")
    public Product getProductByName(String name){
        return productRepository.findProductByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Product not found "));
    }

    @Cacheable("productsCache")
    public List<Product> getAllProducts(int pageNo, int pageSize){
        log.info("Get all products");
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Product> all = productRepository.findAll(paging);
        if (all.hasContent()){
            return all.getContent();
        }
        return null;
    }

    @Cacheable("productsCache")
    public List<Product> getAllProductsBySex(int pageNo, int pageSize, char sex){
        log.info("Get all products by sex");
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Product> all = productRepository.findAllBySex(paging, sex);
        if (all.hasContent()){
            return all.getContent();
        }
        return null;
    }

    public boolean isProductExistById(String id){
        return productRepository.existsById(id);
    }

    @CachePut(value = "productsCache", key = "#product.id")
    public Product save(Product product){
        Product savedProduct = productRepository.save(product);
        kafkaProducer.sendProduct(savedProduct);
        return savedProduct;
    }

    public void saveProducts(List<Product> products){
        Iterable<Product> savedProducts = productRepository.saveAll(products);
        savedProducts.forEach(p -> kafkaProducer.sendProduct(p));
    }

    @CacheEvict(value = "productsCache", key = "#id")
    public void deleteProductById(String id){
        productRepository.deleteById(id);
        log.debug("Product {} is deleted.", id);
    }

    private Product createMockProduct(){
        Set<String> size = new HashSet<>();
        size.add("S");
        size.add("M");
        size.add("L");

        Set<String> colors = new HashSet<>();
        colors.add("Black");
        colors.add("White");

        SizeQte sizeQteS = new SizeQte();
        sizeQteS.setSize('S');
        sizeQteS.setQte(2);

        SizeQte sizeQteM = new SizeQte();
        sizeQteM.setSize('M');
        sizeQteM.setQte(3);

        Set<SizeQte> sizeQtesBlack = new HashSet<>();
        sizeQtesBlack.add(sizeQteS);
        sizeQtesBlack.add(sizeQteM);

        Map<String, Set<SizeQte>> availablities = new HashMap<>();
        availablities.put("Black", sizeQtesBlack);
        availablities.put("White", sizeQtesBlack);


        return Product.builder()
                .id(String.valueOf(Math.random()))
                .name("Sweat - Crew")
                .brand("Nike")
                .logo("https://www.festisite.com/static/partylogo/img/logos/nike.png")
                .sex('W')
                .category("Sweat")
                .price(BigDecimal.valueOf(80))
                .images(Collections.singleton("https://images.asos-media.com/products/nike-logo-crew-sweat-in-white-804340-100/7134528-3?$XXL$&wid=513&fit=constrain"))
                .size(size)
                .colors(colors)
                .availability(availablities)
                .build();

    }
}
