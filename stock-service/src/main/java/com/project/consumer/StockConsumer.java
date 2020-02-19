package com.project.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.Order;
import com.project.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static com.project.utils.ProductCode.PRODUCT_FALLBACK;
import static org.eclipse.microprofile.reactive.messaging.Acknowledgment.Strategy.MANUAL;

@Slf4j
public class StockConsumer {

    @Inject
    private StockService stockService;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Incoming("orders")
    @Acknowledgment(MANUAL)
    public CompletionStage<Void> handleStock(Message<String> order) {
        try {
            Order orderMapper = mapper.readValue(order.getPayload(), Order.class);
            log.info("************* order consumed {}", orderMapper.toString());
            String response = stockService.manageStock(orderMapper);
            if (!response.equals(PRODUCT_FALLBACK.getValue())){
                return order.ack();
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Json processing failed ", e);
        }

        return null;
    }
}
