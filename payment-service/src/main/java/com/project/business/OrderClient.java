package com.project.business;

import com.project.configuration.FeignAuthentication;
import com.project.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "order-service", fallback = OrderClientFallback.class, configuration = FeignAuthentication.class)
public interface OrderClient {

    @PostMapping("/save")
    void saveOrder(@RequestBody Order order);
}
