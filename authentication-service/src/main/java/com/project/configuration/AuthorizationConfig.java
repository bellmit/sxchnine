package com.project.configuration;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
@Order(SecurityProperties.BASIC_AUTH_ORDER)
@CrossOrigin(allowCredentials = "true",methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.OPTIONS})
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    private AuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder;

    private RedisConnectionFactory redisConnectionFactory;

    public AuthorizationConfig(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RedisConnectionFactory redisConnectionFactory) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    /*    @Bean
    public TokenStore tokenStore(){
        return new InMemoryTokenStore();
    }*/

/*    @Bean
    public TokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }*/

    /*
        To support JWT Token
     */
    @Bean
    @Primary
    public DefaultTokenServices defaultTokenServices(){
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(jwtTokenStore());
        defaultTokenServices.setReuseRefreshToken(true);
        return defaultTokenServices;
    }
    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }
    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey("123");
        return accessTokenConverter;
    }
    @Bean
    public TokenEnhancer tokenEnhancer(){
        return new CustomTokenEnhancer();
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clientDetailsServiceConfigurer) throws Exception {
        clientDetailsServiceConfigurer
                .inMemory()
                .withClient("client")
                .secret("{noop}secret")
                .accessTokenValiditySeconds(86400)
                .scopes("all")
                .authorizedGrantTypes("client-credentials", "password","refresh_token");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpointsConfigurer) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
        endpointsConfigurer.tokenStore(jwtTokenStore())
                .authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS)
                .tokenEnhancer(tokenEnhancerChain);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer serverSecurityConfigurer){
        serverSecurityConfigurer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(passwordEncoder);
    }

}
