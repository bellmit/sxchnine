package com.project.controller;

import com.project.config.ResourceServerConfigTest;
import com.project.model.Product;
import com.project.repository.ProductRepository;
import org.jeasy.random.EasyRandom;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@EmbeddedKafka(topics = "products")
@Import(ResourceServerConfigTest.class)
@ActiveProfiles("test")
@DirtiesContext
public class ProductControllerTestIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ProductRepository productRepository;

    private static EmbeddedElastic embeddedElastic;

    @BeforeClass
    public static void setup() throws IOException, InterruptedException {
        int httpPort = findRandomPort();
        int transportPort = findRandomPort();
        System.setProperty("elasticsearch.port", String.valueOf(transportPort));
        embeddedElastic = EmbeddedElastic.builder()
                .withElasticVersion("6.2.2")
                .withSetting(PopularProperties.CLUSTER_NAME, "elasticsearch")
                .withSetting(PopularProperties.TRANSPORT_TCP_PORT, transportPort)
                .withSetting(PopularProperties.HTTP_PORT, httpPort)
                .withDownloadUrl(new URL("https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.2.2.zip"))
                .build()
                .start();
    }

    @Test
    public void testSearchAllProduct(){
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);

        productRepository.save(product);

        ResponseEntity<List<Product>> response = testRestTemplate.exchange("/search/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>() {});

        assertThat(response.getBody()).contains(product);
    }

    @Test
    public void testSearchProduct(){
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);
        product.setBrand("toto");

        productRepository.save(product);

        ResponseEntity<List<Product>> response = testRestTemplate.exchange("/search/toto",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>() {});

        assertThat(response.getBody()).contains(product);

    }

    @Test
    public void testAdvancedSearch(){
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);
        product.setBrand("toto");
        product.setCategory("titi");
        product.setSize("l");

        productRepository.save(product);

        ResponseEntity<List<Product>> response = testRestTemplate.exchange("/advancedSearch?brand=toto&category=titi&size=l",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>() {});

        assertThat(response.getBody()).contains(product);

    }

    @Test
    public void testSave(){
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);

        testRestTemplate.postForObject("/save", product, Product.class);

        Optional<Product> savedProduct = productRepository.findById(product.getId());

        assertThat(savedProduct.get()).isEqualToComparingFieldByFieldRecursively(product);
    }

    @Test
    public void testDeleteById(){
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);
        product.setId("1");

        productRepository.save(product);

        testRestTemplate.delete("/delete/1");

        Optional<Product> savedProduct = productRepository.findById(product.getId());

        assertThat(savedProduct.orElse(null)).isNull();
    }

    @AfterClass
    public static void teardown(){
        embeddedElastic.stop();
    }

    private static int findRandomPort() throws IOException {
        ServerSocket socket = new ServerSocket(0);
        return socket.getLocalPort();
    }
}
