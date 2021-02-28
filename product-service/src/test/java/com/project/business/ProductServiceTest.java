package com.project.business;

import com.project.model.Product;
import com.project.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierExtensionsKt;
import reactor.test.StepVerifierOptions;
import reactor.util.context.Context;
import utils.TestObjectCreator;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testGetProductById(){
        Product product = TestObjectCreator.createProduct();
        when(productRepository.findProductById(1L)).thenReturn(Mono.just(product));
        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(tracer.nextSpan()).thenReturn(span);

        StepVerifierOptions stepVerifierOptions = StepVerifierOptions.create().withInitialContext(context);

        StepVerifier.create(productService.getProductById(1L), stepVerifierOptions)
                .expectNext(product)
                .expectComplete()
                .verify();

        verify(productRepository).findProductById(1L);
    }

    @Test
    public void testGetAllProducts(){
        when(productRepository.findAll()).thenReturn(Flux.empty());
        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(tracer.nextSpan()).thenReturn(span);

        StepVerifierOptions stepVerifierOptions = StepVerifierOptions.create().withInitialContext(context);

        StepVerifier.create(productService.getAllProducts(), stepVerifierOptions)
                .expectComplete()
                .verify();

        verify(productRepository).findAll();
    }

    @Test
    public void testSave(){
        Product product = TestObjectCreator.createProduct();
        when(productRepository.save(product)).thenReturn(Mono.just(product));
        when(kafkaProducer.sendProduct(any())).thenReturn(Mono.just(product));
        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(tracer.nextSpan()).thenReturn(span);

        StepVerifierOptions stepVerifierOptions = StepVerifierOptions.create().withInitialContext(context);

        StepVerifier.create(productService.save(product), stepVerifierOptions)
                .expectNext(product)
                .expectComplete()
                .verify();

        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(argumentCaptor.capture());
        verify(kafkaProducer).sendProduct(any());

    }

    @Test
    public void testDeleteProductById(){
        when(productRepository.deleteById(1L)).thenReturn(Mono.empty());
        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(tracer.nextSpan()).thenReturn(span);

        StepVerifierOptions stepVerifierOptions = StepVerifierOptions.create().withInitialContext(context);

        StepVerifier.create(productService.deleteProductById(1), stepVerifierOptions)
                .expectComplete()
                .verify();
    }

}
