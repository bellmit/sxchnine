package com.project.business;

import com.project.model.Product;
import org.jeasy.random.EasyRandom;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(topics = "products")
@TestPropertySource(properties = {"spring.autoconfigure.exclude=" +
        "org.springframework.cloud.stream.test.binder.TestSupportBinderAutoConfiguration"})
@DirtiesContext
public class ProductServiceTestIT {

    @Autowired
    private ProductService productService;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, "orders");


    private static EmbeddedElastic embeddedElastic;

    @BeforeClass
    public static void setup() throws IOException, InterruptedException {
        int port = findRandomPort();
        int transportPort = findRandomPort();
        System.setProperty("elasticsearch.port", String.valueOf(transportPort));
        embeddedElastic = EmbeddedElastic.builder()
                .withElasticVersion("6.2.2")
                .withSetting(PopularProperties.TRANSPORT_TCP_PORT, transportPort)
                .withSetting(PopularProperties.HTTP_PORT, port)
                .withSetting(PopularProperties.CLUSTER_NAME, "elasticsearch")
                .withDownloadUrl(new URL("https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.2.2.zip"))
                .build()
                .start();
    }

    @Test
    public void testGetProductsByQuery(){
        Product product = new Product();
        product.setName("classic bob nike");
        product.setCategory("t-shirt");
        product.setBrand("nike");

        productService.save(product);

        Iterable<Product> result = productService.getProductsByQuery("nike");

        assertThat(result).contains(product);
    }

    @Test
    public void testGetProductsByAdvancedFilteringSearchBrand(){
        Product product = new Product();
        product.setName("classic bob nike");
        product.setCategory("t-shirt");
        product.setBrand("nike");

        Product product2 = new Product();
        product2.setName("retro adidas");
        product2.setCategory("t-shirt");
        product2.setBrand("adidas");

        productService.save(product);
        productService.save(product2);

        Iterable<Product> result = productService.getProductsByAdvancedFiltering("M","nike", "", "");

        assertThat(result).contains(product);
    }


    @Test
    public void testGetProductsByAdvancedFilteringSearchCategory(){
        Product product = new Product();
        product.setName("classic bob nike");
        product.setCategory("t-shirt");
        product.setBrand("nike");

        Product product2 = new Product();
        product2.setName("retro adidas");
        product2.setCategory("t-shirt");
        product2.setBrand("adidas");

        productService.save(product);
        productService.save(product2);

        Iterable<Product> result = productService.getProductsByAdvancedFiltering("","", "t-shirt", "");

        assertThat(result).contains(product);
        assertThat(result).contains(product2);
    }

    @Test
    public void testGetProductsByAdvancedFilteringSearchSize(){
        Product product = new Product();
        product.setName("classic bob nike");
        product.setCategory("t-shirt");
        product.setBrand("nike");
        product.setSize(Collections.singletonList("M"));

        Product product2 = new Product();
        product2.setName("retro adidas");
        product2.setCategory("t-shirt");
        product2.setBrand("adidas");
        product2.setSize(Collections.singletonList("L"));

        productService.save(product);
        productService.save(product2);

        Iterable<Product> result = productService.getProductsByAdvancedFiltering("","", "", "L");

        assertThat(result).contains(product2);
    }

    @Test
    public void testGetProductsByAdvancedFiltering(){
        Product product = new Product();
        product.setName("classic bob nike");
        product.setCategory("t-shirt");
        product.setBrand("nike");
        product.setSize(Collections.singletonList("M"));

        Product product2 = new Product();
        product2.setName("retro adidas");
        product2.setCategory("t-shirt");
        product2.setBrand("adidas");
        product2.setSize(Collections.singletonList("L"));

        productService.save(product);
        productService.save(product2);

        Iterable<Product> result = productService.getProductsByAdvancedFiltering("", "nike", "t-shirt", "M");

        assertThat(result).contains(product);
    }

    @Test
    public void testGetProductsByAdvancedFilteringNotFound(){
        Product product = new Product();
        product.setName("classic bob nike");
        product.setCategory("t-shirt");
        product.setBrand("nike");
        product.setSize(Collections.singletonList("M"));

        productService.save(product);

        Iterable<Product> result = productService.getProductsByAdvancedFiltering("", "nike", "cap", "M");

        assertThat(result).doesNotContain(product);
    }

    @Test
    public void testSave(){
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);

        productService.save(product);

        Iterable<Product> savedProduct = productService.getProductsByAdvancedFiltering(product.getSex(), product.getBrand(), product.getCategory(), product.getSize().get(0));

        assertThat(savedProduct).contains(product);
    }

    @Test
    public void testDeleteProductById(){
        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);

        productService.save(product);

        productService.deleteById(product.getId());

        Iterable<Product> savedProduct = productService.getProductsByAdvancedFiltering(product.getSex(), product.getBrand(), product.getCategory(), product.getSize().get(0));

        assertThat(savedProduct).isEmpty();
    }

    @AfterClass
    public static void teardown(){
        embeddedElastic.stop();
    }

    private static Integer findRandomPort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }
}
