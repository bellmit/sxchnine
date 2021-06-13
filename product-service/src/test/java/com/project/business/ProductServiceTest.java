package com.project.business;

import com.project.model.Product;
import com.project.repository.ProductRepository;
import org.apache.commons.lang3.RegExUtils;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;
import reactor.util.context.Context;
import utils.TestObjectCreator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testGetProductById() {
        Product product = TestObjectCreator.createProduct();
        when(productRepository.findProductById(1L)).thenReturn(Mono.just(product));
        // Mocking Sleuth vs Reactor Context
        // Disable if you want to log the action related to the function

        /*Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(tracer.nextSpan()).thenReturn(span);*/

        StepVerifierOptions stepVerifierOptions = StepVerifierOptions.create();

        StepVerifier.create(productService.getProductById(1L), stepVerifierOptions)
                .expectNext(product)
                .expectComplete()
                .verify();

        verify(productRepository).findProductById(1L);
    }

    @Test
    public void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Flux.empty());
        // Mocking Sleuth vs Reactor Context
        // Disable if you want to log the action related to the function
/*        Context context = mock(Context.class);
        TraceContext traceContext = mock(TraceContext.class);
        CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(span.context()).thenReturn(traceContext);
        when(context.get(any())).thenReturn(currentTraceContext).thenReturn(tracer);
        when(tracer.nextSpan()).thenReturn(span);*/

        StepVerifierOptions stepVerifierOptions = StepVerifierOptions.create();

        StepVerifier.create(productService.getAllProducts(), stepVerifierOptions)
                .expectComplete()
                .verify();

        verify(productRepository).findAll();
    }

    @Test
    public void testSave() {
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
    public void testDeleteProductById() {
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

    @Test
    public void testRegexOnMap(){
        Map<String, String> testMap = new HashMap<>();
        testMap.put("\"Orange\"", "orange");
        testMap.put("\"Toffe\"", "tofee");
        testMap.put("black", "black");

        testMap.keySet().removeIf(t -> Pattern.compile("([\"])\\w+").asPredicate().test(t));

        testMap.keySet().forEach(System.out::println);
    }

    @Test
    public void test(){

        Stream<Integer> integerStream = Stream.of(1, 2, 3);

        //integerStream.map(p -> p + 2).forEach(System.out::println);

        List<Integer> integers = List.of(1, 2, 3);

        /*Supplier<String> supplier = new Supplier<String>() {

            {
                System.out.println("block of foo supplier");
            }
            @Override
            public String get() {
                System.out.println("execute supplier");
                return "foo supplier";
            }
        };*/
        Optional<String> foo = Optional.of("FOO");
        String toto = foo.orElseGet(new Supplier<String>() {

            {
                System.out.println("block of foo supplier");
            }
            @Override
            public String get() {
                System.out.println("execute supplier");
                return "foo supplier";
            }
        });
        //String toto = foo.orElse(defaultNethod());
        //System.out.println(toto);

        //Stream.of(defaultNethod());

        var p = new Product();

        p = new Product();
    }

    private String defaultNethod(){
        System.out.println("execute default ,ethode");
        return "DEFAULT";
    }

    public class FooSupplier implements Supplier<String> {



        @Override
        public String get() {
            return "foo supplier";
        }
    }

}
