package com.project.controller;

import com.project.business.OrderIdService;
import com.project.business.OrderService;
import com.project.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE;

@RestController
@Slf4j
public class OrderController {

    private final OrderService orderService;

    private final OrderIdService orderIdService;

    public OrderController(OrderService orderService, OrderIdService orderIdService) {
        this.orderService = orderService;
        this.orderIdService = orderIdService;
    }

    @GetMapping(value = "/all", produces = APPLICATION_STREAM_JSON_VALUE)
    public Flux<Order> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/orderId/{orderId}")
    public Mono<OrderId> getOrdersByOrderId(@PathVariable String orderId){
        return orderIdService.getOrderByOrderId(orderId);
    }

    @GetMapping(value = "/userEmail/{userEmail:.+}")
    public Flux<Order> getOrdersByEmail(@PathVariable String userEmail){
        return orderService.getOrderByUserEmail(userEmail);
    }

    @PostMapping("/checkoutOrder")
    public Mono<PaymentResponse> checkoutOrderAndSave(@RequestBody Order order){
        return orderService.checkoutOrderAndSave(order);
    }

    @PostMapping("/save")
    public Mono<Void> saveOrder(@RequestBody Order order){
        //TODO: delete simulationOrder
        return orderService.saveOrder(order);
    }

    public Order simulationOrder(){
        Order order = new Order();
        OrderPrimaryKey primaryKey = new OrderPrimaryKey();
        primaryKey.setOrderId("1234");
        primaryKey.setUserEmail("blindrider400@gmail.com");
        primaryKey.setOrderTime(LocalDateTime.now().withNano(0));
        primaryKey.setShippingTime(LocalDateTime.now().withNano(0));
        order.setOrderPrimaryKey(primaryKey);

        Product product1 = new Product();
        product1.setProductId("Product1");
        product1.setProductName("Classic Retro Nike Jacket");
        product1.setProductBrand("Nike");
        product1.setProductColor("black");
        product1.setProductSize("M");
        product1.setProductQte(1);
        product1.setUnitPrice(new BigDecimal(100));
        product1.setStore("CA");

        Product product2 = new Product();
        product2.setProductId("Product2");
        product2.setProductName("Classic Reebok");
        product2.setProductBrand("Reebok");
        product2.setProductColor("white");
        product2.setProductSize("45");
        product2.setProductQte(1);
        product2.setUnitPrice(new BigDecimal(100));
        product2.setStore("CA");

        List<Product> products = Arrays.asList(product1, product2);
        order.setProducts(products);
        order.setProductBrand(products.stream().map(Product::getProductBrand).collect(Collectors.joining(",")));
        order.setProductName(products.stream().map(Product::getProductName).collect(Collectors.joining(",")));

        order.setOrderStatus("PENDING");
        order.setPaymentStatus("WAITING");

        order.setPaymentTime(LocalDateTime.now().withNano(0));
        order.setShippingStatus("PENDING");
        Address address = new Address();
        address.setAddress("Av POP EST");
        address.setCity("Montréal");
        address.setPostalCode("H2FP2P");
        address.setProvince("QC");
        address.setCountry("CA");
        order.setUserAddress(address);

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setNoCreditCard("123456789");
        paymentInfo.setExpDate("O5/22");
        paymentInfo.setSecurityCode(123);
        paymentInfo.setLastName("TOTO");
        paymentInfo.setFirstName("TATA");
        order.setPaymentInfo(paymentInfo);

        return order;
    }
}
