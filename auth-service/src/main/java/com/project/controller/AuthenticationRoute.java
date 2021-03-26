package com.project.controller;

import com.project.business.AuthenticationService;
import io.quarkus.vertx.web.Route;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class AuthenticationRoute {

    @Inject
    AuthenticationService authenticationService;

    @Route(path = "/authenticate", methods = HttpMethod.GET, produces = "application/json")
    public Uni<JsonObject> authenticate(){
        return authenticationService.authenticate();
    }

}
