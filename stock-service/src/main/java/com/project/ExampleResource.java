package com.project;

import com.project.client.ProductService;
import com.project.client.model.Product;
import com.project.model.SizeQte;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.*;

@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class ExampleResource {

    @Inject
    @RestClient
    private ProductService productService;

    @Path("/id")
    @GET
    public void id(){
        log.info(productService.getProductsByIds(Arrays.asList("XXA0.4849661156448658", "2")).toString());

    }

    @Path("save")
    @POST
    public void hello(Product product) {
        Set<String> size = new HashSet<>();
        size.add("S");
        size.add("M");
        size.add("L");

        Set<String> colors = new HashSet<>();
        colors.add("Red");
        colors.add("Gray");

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
        availablities.put("Red", sizeQtesBlack);
        availablities.put("Gray", sizeQtesBlack);

        Product p = Product.builder()
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

        productService.createOrUpdateProducts(Arrays.asList(p));

    }
}