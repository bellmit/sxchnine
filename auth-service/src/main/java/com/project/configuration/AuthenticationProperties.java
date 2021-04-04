package com.project.configuration;

import io.quarkus.arc.config.ConfigProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ConfigProperties(prefix = "authentication")
public class AuthenticationProperties {

    private String host;
    private int port;
    private String uri;
    private String grantType;
    private String clientId;
    private String secret;
    private String scope;
    private String user;
    private String pwd;


}
