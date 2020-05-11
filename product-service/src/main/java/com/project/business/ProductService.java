package com.project.business;


import com.hazelcast.core.HazelcastInstance;
import com.project.model.Product;
import com.project.model.SizeQte;
import com.project.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.cache.CacheMono;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

import java.math.BigDecimal;
import java.util.*;


@Service
@Slf4j
public class ProductService {

    private ProductRepository productRepository;

    private KafkaProducer kafkaProducer;

    private HazelcastInstance hazelcastInstance;

    public ProductService(ProductRepository productRepository, KafkaProducer kafkaProducer, HazelcastInstance hazelcastInstance) {
        this.productRepository = productRepository;
        this.kafkaProducer = kafkaProducer;
        this.hazelcastInstance = hazelcastInstance;
    }

    //@Cacheable(value = "productsCache", key = "#id", unless = "#result==null")
    public Mono<Product> getProductById(String id){
        CacheMono
                .lookup(k -> productRepository.findById(id).map(Signal::next), id)
                .onCacheMissResume(Mono.just(new Product()))
                .andWriteWith((k, sig) -> {
                    hazelcastInstance.getMap("productsCache").computeIfAbsent(id, v -> sig.get());
                    return Mono.empty();
                });
        return productRepository.findById(id).onErrorReturn(new Product());
    }

    public Flux<Product> getProductByIds(List<String> ids){
        return productRepository.findProductsByIdIn(ids);
    }

    //@Cacheable(value = "productsCache", key = "#name", unless = "#result==null")
    public Mono<Product> getProductByName(String name){
        return productRepository.findProductByName(name).onErrorReturn(new Product());
    }

    //@Cacheable("productsCache")
    public Flux<Product> getAllProducts(int pageNo, int pageSize){
        log.info("Get all products");
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return productRepository.findAll();

    }

    //@Cacheable("productsCache")
    public Flux<Product> getAllProductsBySex(int pageNo, int pageSize, char sex){
        log.info("Get all products by sex");
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return productRepository.findAllBySex(sex, paging);
    }

    public Mono<Boolean> isProductExistById(String id){
        return productRepository.existsById(id);
    }

    //@CachePut(value = "productsCache", key = "#product.id")
    public Mono<Product> save(Product product){
        Mono<Product> savedProduct = productRepository.save(product);
        savedProduct.subscribe(p -> kafkaProducer.sendProduct(p));
        return savedProduct;
    }

    public void saveProducts(List<Product> products){
        Flux<Product> savedProducts = productRepository.saveAll(products);
        savedProducts.subscribe(p -> kafkaProducer.sendProduct(p));
    }

    //@CacheEvict(value = "productsCache", key = "#id")
    public Mono<Void> deleteProductById(String id){
        log.debug("Product {}  delete ", id);
        return productRepository.deleteById(id);
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
