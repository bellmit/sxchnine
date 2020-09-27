package com.project.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.Order;
import com.project.service.StockService;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.eclipse.microprofile.reactive.messaging.Acknowledgment.Strategy.MANUAL;

@ApplicationScoped
@Slf4j
public class StockConsumer {

    @Inject
    StockService stockService;

    private static final ObjectMapper mapper = new ObjectMapper();

    private final Set<String> ordersToCatchup = new CopyOnWriteArraySet<>();

    @Incoming("orders")
    @Acknowledgment(MANUAL)
    public Uni<Void> handleStock(Message<Order> order) {
            log.info("************* order consumed {}", order.getPayload());
            return Uni.createFrom().item(order)
                    .onItem()
                    .transformToUni(o -> stockService.manageStock(o.getPayload()))
                    .flatMap(m -> {
                        if (!m.equals("SUCCESS")) {
                            log.info("stock managed with {} for order {}", m, order.getPayload());
                            // TODO: handle error orders
                            //ordersToCatchup.add(order.getPayload());
                        }
                        return Uni.createFrom().completionStage(order.ack());
                    });
    }
}
