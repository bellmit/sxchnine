package com.project.business;

import com.project.configuration.AuthenticationProperties;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.ext.web.multipart.MultipartForm;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Random;

@ApplicationScoped
public class AuthenticationService {

    private static final Random RANDOM = new Random();

    @Inject
    Vertx vertx;

    @Inject
    AuthenticationProperties authenticationProperties;

    WebClient webClient;

    @PostConstruct
    public void initWebClient() {
        this.webClient = WebClient.create(vertx, new WebClientOptions()
                .setDefaultHost(authenticationProperties.getHost())
                .setDefaultPort(authenticationProperties.getPort())
                .setSsl(true)
                .setTrustAll(false));
    }

    public Uni<String> authenticate() {
        MultipartForm form = MultipartForm.create();
        form.attribute("grant_type", authenticationProperties.getGrantType());
        form.attribute("client_id", authenticationProperties.getClientId());
        form.attribute("client_secret", authenticationProperties.getSecret());
        form.attribute("scope", authenticationProperties.getScope());
        form.attribute("user", authenticationProperties.getUser());
        form.attribute("password", authenticationProperties.getPwd());

        return webClient.post(authenticationProperties.getUri())
                .ssl(true)
                .putHeader("Content-Type", "application/x-www-form-urlencoded")
                .sendMultipartForm(form)
                .map(HttpResponse::bodyAsJsonObject)
                .map(json -> json.getString("access_token")
                        .concat("_")
                        .concat(generateRandomInts()));
    }

    private String generateRandomInts() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 20;

        return RANDOM.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString()
                .toUpperCase();
    }
}
