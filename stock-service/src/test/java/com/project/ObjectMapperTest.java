/*
package com.project;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.Order;
import org.junit.Assert;
import org.junit.Test;

public class ObjectMapperTest {

    @Test
    public void testMapper() throws JsonProcessingException {
        String order = "{\"orderPrimaryKey\":{\"userEmail\":\"pop@pp.com\",\"orderId\":\"e5d0ac70-4708-11ea-840b-8105a25d71b3\",\"orderTime\":\"2020-02-03 23:43:32\",\"shippingTime\":\"2020-02-03 23:43:32\"},\"products\":[{\"productName\":null,\"productBrand\":null,\"productId\":null,\"productSize\":null,\"productColor\":null,\"productQte\":0,\"unitPrice\":null,\"store\":null}],\"productBrand\":null,\"productName\":null,\"total\":0,\"paymentInfo\":{\"noCreditCard\":\"9009 1188 2773 8810\",\"expDate\":\"11/11\",\"securityCode\":123,\"lastName\":\"soup\",\"firstName\":null},\"userAddress\":{\"address\":\"4433 rue Clark\",\"postalCode\":\"O11PO2\",\"city\":\"MTL\",\"province\":null,\"country\":\"CA\"},\"orderStatus\":\"UNKNOWN\",\"paymentStatus\":\"WAITING\",\"paymentTime\":\"2020-02-03 23:43:32\",\"shippingStatus\":\"UNKNOWN\"}";

        ObjectMapper mapper = new ObjectMapper();
        Order orderMapper = mapper.readValue(order, Order.class);
        Assert.assertEquals("pop@pp.com", orderMapper.getOrderPrimaryKey().getUserEmail());
    }
}
*/
