package com.project.business;

import com.project.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.context.Context;
import utils.TestObjectCreator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@EmbeddedKafka
@ActiveProfiles("test")
@TestInstance(PER_CLASS)
@DirtiesContext
public class ProductServiceTestIT {

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Autowired
    private ProductService productService;

    @MockBean
    private KafkaProducer kafkaProducer;

    @AfterEach
    public void dropCollection() {
        mongoTemplate.dropCollection("products").block();
    }

    @Test
    public void testGetProductById() {
        Mono<Product> products = mongoTemplate.createCollection("products")
                .then(mongoTemplate.save(TestObjectCreator.createProduct()))
                .then(productService.getProductById(1L));

        StepVerifier.create(products)
                .expectSubscription()
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetProductByName() {
        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(tracer.nextSpan()).thenReturn(span);

        Product product = mongoTemplate.createCollection("products")
                .then(mongoTemplate.save(TestObjectCreator.createProduct()))
                .then(productService.getProductByName("p1"))
                .contextWrite(ctx -> context)
                .block();

        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(1L);

    }

    @Test
    public void testGetAllProducts() {
        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(tracer.nextSpan()).thenReturn(span);

        Product productFlux = mongoTemplate.save(TestObjectCreator.createProduct())
                .thenMany(productService.getAllProducts())
                .contextWrite(ctx -> context)
                .blockFirst();

        assertThat(productFlux.getId()).isEqualTo(1L);
    }

    @Test
    public void testSaveProduct() {
        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(tracer.nextSpan()).thenReturn(span);

        when(kafkaProducer.sendProduct(any())).thenReturn(Mono.just(TestObjectCreator.createProduct()));

        productService.save(TestObjectCreator.createProduct())
                .flatMap(p -> productService.getProductById(1L))
                .contextWrite(ctx -> context)
                .block();

        verify(kafkaProducer).sendProduct(any());

    }

    @Test
    public void testDeleteProductById() {
        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(tracer.nextSpan()).thenReturn(span);

        Product product = mongoTemplate.save(TestObjectCreator.createProduct())
                .flatMap(p -> productService.deleteProductById(1))
                .then(productService.getProductById(1L))
                .contextWrite(ctx -> context)
                .block();

        assertThat(product).isNull();

    }
}
