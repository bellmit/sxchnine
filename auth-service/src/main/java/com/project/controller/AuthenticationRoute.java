package com.project.controller;

import com.project.business.AuthenticationService;
import com.project.model.ErrorResponse;
import io.quarkus.vertx.web.Route;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@ApplicationScoped
@Slf4j
public class AuthenticationRoute {

    @Inject
    AuthenticationService authenticationService;

    @ConfigProperty(name = "authentication.domainUi")
    List<String> domains;

    @ConfigProperty(name = "authentication.secret")
    String secret;

    @Route(path = "/authenticate", methods = HttpMethod.GET, produces = "application/json")
    public Uni<String> authenticate(RoutingContext routingContext) {
        if (validateDomain(routingContext) || validateSecret(routingContext)) {
            routingContext.response().setStatusCode(401);
            return Uni.createFrom().item("not authorized");
        } else {
            return authenticationService.authenticate();
        }
    }

    private boolean validateDomain(RoutingContext routingContext) {
        return routingContext.request().headers().get("X-DOMAIN") == null
                || !domains.contains(routingContext.request().headers().get("X-DOMAIN"));
    }

    private boolean validateSecret(RoutingContext routingContext) {
        return routingContext.request().headers().get("X-AUTHORITY") == null
                || !routingContext.request().headers().get("X-AUTHORITY").contains(secret)
                || !validateTimestamp(routingContext.request().headers().get("X-AUTHORITY"));
    }

    private boolean validateTimestamp(String authority){
        try {
            String[] split = authority.split("-");
            LocalDateTime receivedTs = LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(split[0])), ZoneId.systemDefault());
            LocalDateTime now = LocalDateTime.now();

            Duration difference = Duration.between(receivedTs, now);

            return difference.toDays() == 0
                    && difference.toHours() == 0
                    && difference.toMinutes() <= 2;

        } catch (Exception e){
            log.error("error parsing authority to extract timestamp", e);

            return false;
        }
    }

}
