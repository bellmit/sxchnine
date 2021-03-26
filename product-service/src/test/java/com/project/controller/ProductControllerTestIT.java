package com.project.controller;

import com.project.business.KafkaProducer;
import com.project.model.Product;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;
import utils.TestObjectCreator;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("intg")
@TestPropertySource(properties = {"spring.autoconfigure.exclude=" +
        "org.springframework.cloud.stream.test.binder.TestSupportBinderAutoConfiguration"
        + "org.springframework.cloud.sleuth.autoconfig.brave.BraveAutoConfiguration"
        + "org.springframework.cloud.sleuth.autoconfig.zipkin2.ZipkinAutoConfiguration"
        + "org.springframework.cloud.sleuth.autoconfig.instrument.reactor.TraceReactorAutoConfiguration"
        + "org.springframework.cloud.sleuth.autoconfig.instrument.reactor.TraceReactorAutoConfiguration.TraceReactorAutoConfigurationAccessorConfiguration"
        + "org.springframework.cloud.sleuth.autoconfig.instrument.web.TraceWebFluxConfiguration"
        + "org.springframework.cloud.sleuth.autoconfig.zipkin2.ZipkinRestTemplateSenderConfiguration"
})
@EmbeddedKafka
@TestInstance(PER_CLASS)
@DirtiesContext
public class ProductControllerTestIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @MockBean
    Context context;

    @BeforeAll
    public void setup() {
        Product product = TestObjectCreator.createProduct();
        mongoTemplate.save(product).block();
        Hooks.resetOnEachOperator();
        Hooks.resetOnLastOperator();
        Schedulers.resetOnScheduleHooks();
    }

    @Test
    public void testGetProductById() {
        webTestClient.get()
                .uri("/id/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Product.class);
    }


    @Test
    public void testGetProductByName() {
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.getOrDefault(any(), any())).thenReturn(null);
        when(context.get(any())).thenReturn(currentTraceContext);
        when(tracer.nextSpan()).thenReturn(span);

        Product product = webTestClient.get()
                .uri("/name/p1")
                .accept(MediaType.ALL)
                .exchange()
                .returnResult(Product.class)
                .getResponseBody()
                .contextWrite(ctx -> context)
                .blockFirst();

        assertThat(product).isNotNull();
    }

    @Disabled
    @Test
    public void testSaveProduct() {
        Product product = TestObjectCreator.createProduct();
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.getOrDefault(any(), any())).thenReturn(null);
        when(context.get(any())).thenReturn(currentTraceContext);
        when(tracer.nextSpan()).thenReturn(span);

        webTestClient.post()
                .uri("/save")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(product), Product.class)
                .exchange()
                .returnResult(Product.class)
                .getResponseBody()
                .contextWrite(ctx -> context);

        Consumer<String, Product> kafkaConsumer = createKafkaConsumer();
        ConsumerRecord<String, Product> singleRecord = KafkaTestUtils.getSingleRecord(kafkaConsumer, "products");

        assertThat(singleRecord.value().getId()).isEqualTo(1L);


    }

    @AfterAll
    public void tearDown() {
        mongoTemplate.dropCollection("products");
    }

    private Consumer<String, Product> createKafkaConsumer() {
        Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps("sender",
                "false", embeddedKafkaBroker);

        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JsonDeserializer<Product> jsonDeserializer = new JsonDeserializer<>(Product.class);
        jsonDeserializer.addTrustedPackages("*");
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);

        DefaultKafkaConsumerFactory<String, Product> consumerFactory = new DefaultKafkaConsumerFactory<>(
                consumerProperties);

        consumerFactory.setKeyDeserializer(new StringDeserializer());
        consumerFactory.setValueDeserializer(jsonDeserializer);
        Consumer<String, Product> consumer = consumerFactory.createConsumer();
        consumer.subscribe(Collections.singleton("products"));
        return consumer;
    }

}
