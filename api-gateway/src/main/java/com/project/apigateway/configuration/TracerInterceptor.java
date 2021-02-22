/*
package com.project.apigateway.configuration;

import io.opentracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class TracerInterceptor extends AbstractGatewayFilterFactory {

    private final Tracer tracer;

*/
/*    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        MDC.put("statusCode", String.valueOf(serverWebExchange.getResponse().getStatusCode().value()));
        MDC.put("traceId", tracer.activeSpan().context().toTraceId());
        MDC.put("spanId", tracer.activeSpan().context().toSpanId());
        return webFilterChain.filter(serverWebExchange);
    }*//*


*/
/*    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        MDC.put("statusCode", String.valueOf(exchange.getResponse().getStatusCode().value()));
        MDC.put("traceId", tracer.activeSpan().context().toTraceId());
        //MDC.put("spanId", tracer.activeSpan().context().toSpanId());
        return chain.filter(exchange);
    }*//*


    @Override
    public GatewayFilter apply(Consumer consumer) {
        return (exchange, chain) -> chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    MDC.put("statusCode", String.valueOf(exchange.getResponse().getStatusCode().value()));
                    MDC.put("traceId", tracer.activeSpan().context().toTraceId());
                    MDC.put("spanId", tracer.activeSpan().context().toSpanId());
                }));
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    MDC.put("statusCode", String.valueOf(exchange.getResponse().getStatusCode().value()));
                    MDC.put("traceId", tracer.activeSpan().context().toTraceId());
                    MDC.put("spanId", tracer.activeSpan().context().toSpanId());
                    log.info("filter qpplied");
                }));
    }
}
*/
