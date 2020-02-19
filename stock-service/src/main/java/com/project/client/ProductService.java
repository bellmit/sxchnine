package com.project.client;


import com.project.client.model.Product;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import java.util.Collections;
import java.util.List;

import static com.project.utils.ProductCode.PRODUCT_FALLBACK;

@RegisterRestClient(configKey = "product-api")
public interface  ProductService {

    @GET
    @Path("/ids")
    @Produces("application/json")
    @Fallback(fallbackMethod = "getProductsByIdsFallback")
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio=0.75, delay = 1000, successThreshold = 10)
    List<Product> getProductsByIds(@QueryParam("ids") List<String> ids);

    @POST
    @Path("/bulk")
    @Fallback(fallbackMethod = "createOrUpdateProductsFallback")
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio=0.75, delay = 1000, successThreshold = 10)
    void createOrUpdateProducts(List<Product> products);

    default List<Product> getProductsByIdsFallback(List<String> ids){
        return Collections.singletonList(Product.builder().id(PRODUCT_FALLBACK.getValue()).build());
    }

    default void createOrUpdateProductsFallback(List<Product> products){
        //TODO: put it on the queue
    }
}
