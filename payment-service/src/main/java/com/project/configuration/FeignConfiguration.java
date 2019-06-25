package com.project.configuration;


import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class FeignConfiguration {

    @Value("${consumer.feign.username}")
    private String username;
    @Value("${consumer.feign.password}")
    private String password;
    @Value("${consumer.feign.url}")
    private String url;
    @Value("${consumer.feign.clientId}")
    private String clientId;
    @Value("${consumer.feign.secret}")
    private String secret;
    @Value("${consumer.feign.grant}")
    private String grant;
    @Value("${consumer.feign.scope}")
    private String scope;


    public RequestInterceptor requestInterceptor(){
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), resource());
    }

    private OAuth2ProtectedResourceDetails resource() {
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setUsername(username);
        resourceDetails.setPassword(password);
        resourceDetails.setAccessTokenUri(url);
        resourceDetails.setClientId(clientId);
        resourceDetails.setClientSecret(secret);
        resourceDetails.setGrantType(grant);
        resourceDetails.setScope(Collections.singletonList(scope));
        return resourceDetails;
    }
}