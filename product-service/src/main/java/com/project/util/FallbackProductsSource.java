package com.project.util;

import com.project.model.AbstractProduct;
import com.project.model.Product;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Set;

import static java.lang.Double.parseDouble;
import static java.math.BigDecimal.valueOf;

@Component
public class FallbackProductsSource {

    private static FallbackProductsConfiguration fallbackProductsConfiguration;

    public FallbackProductsSource(FallbackProductsConfiguration fallbackProductsConfiguration) {
        FallbackProductsSource.fallbackProductsConfiguration = fallbackProductsConfiguration;
    }

    public static Flux<Product> fallbackProducts(char sex) {
        if (sex == 'M') {
            return createFallbackProducts(fallbackProductsConfiguration.getMen(), sex);
        } else {
            return createFallbackProducts(fallbackProductsConfiguration.getWomen(), sex);
        }
    }

    private static Flux<Product> createFallbackProducts(AbstractProduct product, char sex) {
        Flux<String> ids = Flux.fromArray(product.getId().split(","));
        Flux<String> names = Flux.fromArray(product.getName().split(","));
        Flux<String> brands = Flux.fromArray(product.getBrand().split(","));
        Flux<String> prices = Flux.fromArray(product.getPrice().split(","));
        Flux<String> size = Flux.fromArray(product.getSize().split(","));
        Flux<String> colors = Flux.fromArray(product.getColors().split(","));
        Flux<String> logos = Flux.fromArray(product.getLogo().split(","));
        Flux<String> images = Flux.fromArray(product.getImages().split(","));

        return Flux.zip(ids, names, brands, prices, size, colors, logos, images)
                .map(p -> Product.builder()
                        .id(Long.parseLong(p.getT1()))
                        .name(p.getT2())
                        .brand(p.getT3())
                        .price(valueOf(parseDouble(p.getT4())))
                        .size(Set.of(p.getT5()))
                        .colors(Set.of(p.getT6()))
                        .logo(p.getT7())
                        .images(Set.of(p.getT7()))
                        .sex(sex)
                        .store("CA")
                        .available(true)
                        .category("All")
                        .quantity(1)
                        .dateTime(LocalDateTime.now().toString())
                        .build());
    }
}
