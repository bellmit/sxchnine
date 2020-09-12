package com.project.business;

import com.project.configuration.AuthenticationProperties;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.ext.web.multipart.MultipartForm;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class AuthenticationService {

    @Inject
    Vertx vertx;

    @Inject
    AuthenticationProperties authenticationProperties;

    WebClient webClient;

    @PostConstruct
    public void initWebClient() {
        this.webClient = WebClient.create(vertx, new WebClientOptions()
                .setDefaultHost(authenticationProperties.getHost()).setDefaultPort(authenticationProperties.getPort()).setTrustAll(true));
    }

    public Uni<JsonObject> authenticate() {
        MultipartForm form = MultipartForm.create();
        form.attribute("grant_type", authenticationProperties.getGrantType());
        form.attribute("client_id", authenticationProperties.getClientId());
        form.attribute("client_secret", authenticationProperties.getSecret());
        form.attribute("scope", authenticationProperties.getScope());
        form.attribute("user", authenticationProperties.getUser());
        form.attribute("password", authenticationProperties.getPassword());

        return webClient.post(authenticationProperties.getUri())
                .putHeader("Content-Type", "application/x-www-form-urlencoded")
                .sendMultipartForm(form)
                .map(HttpResponse::bodyAsJsonObject);
    }
}
