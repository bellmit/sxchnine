package com.project.business;

import com.project.model.Product;
import org.jeasy.random.EasyRandom;
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
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;
import reactor.util.context.Context;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ReactiveElasticsearchOperations reactiveElasticsearchOperations;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testGetProductsByQuery() {
        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(tracer.nextSpan()).thenReturn(span);

        when(reactiveElasticsearchOperations.search(any(), eq(Product.class), eq(Product.class)))
                .thenReturn(Flux.empty());

        StepVerifierOptions stepVerifierOptions = StepVerifierOptions.create().withInitialContext(context);
        StepVerifier.create(productService.getProductsByQuery("test"), stepVerifierOptions)
                .expectComplete()
                .verify();

        verify(reactiveElasticsearchOperations).search(any(), eq(Product.class), eq(Product.class));
    }

    @Test
    public void testGetProductsByAdvancedFiltering() {
        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(tracer.nextSpan()).thenReturn(span);

        when(reactiveElasticsearchOperations.search(any(), eq(Product.class), eq(Product.class))).thenReturn(Flux.empty());

        StepVerifierOptions stepVerifierOptions = StepVerifierOptions.create().withInitialContext(context);

        StepVerifier.create(productService
                .getProductsByAdvancedFiltering("",
                        "brand",
                        "category",
                        "size"), stepVerifierOptions)
                .expectComplete()
                .verify();

        verify(reactiveElasticsearchOperations).search(any(), eq(Product.class), eq(Product.class));
    }

    @Test
    public void testSave() {
        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);

        EasyRandom easyRandom = new EasyRandom();
        Product product = easyRandom.nextObject(Product.class);

        when(reactiveElasticsearchOperations.save(any(Product.class))).thenReturn(Mono.empty());

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        StepVerifierOptions stepVerifierOptions = StepVerifierOptions.create().withInitialContext(context);

       StepVerifier.create(productService.save(product), stepVerifierOptions)
               .expectComplete()
               .verify();

        verify(reactiveElasticsearchOperations).save(productCaptor.capture());

        assertThat(productCaptor.getValue()).usingRecursiveComparison().isEqualTo(product);
    }

    @Test
    public void testDeleteById() {
        // Mocking Sleuth vs Reactor Context
        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);

        when(reactiveElasticsearchOperations.delete(anyString(), eq(Product.class))).thenReturn(Mono.empty());

        StepVerifierOptions stepVerifierOptions = StepVerifierOptions.create().withInitialContext(context);

        StepVerifier.create(productService.deleteById("id"), stepVerifierOptions)
                .expectComplete()
                .verify();

        verify(reactiveElasticsearchOperations).delete(eq("id"), eq(Product.class));
    }

}
