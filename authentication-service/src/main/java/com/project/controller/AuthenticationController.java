package com.project.controller;

import com.project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DefaultTokenServices jwtTokenStore;



    @PostMapping("/signup")
    public boolean signUp(@RequestBody User user){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
/*
        OAuth2Authentication authenticate = (OAuth2Authentication) authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        OAuth2AccessToken accessToken = jwtTokenStore.getAccessToken(authenticate);
*/


      /*  RestTemplate restTemplate = new RestTemplate();

        // According OAuth documentation we need to send the client id and secret key in the header for authentication
        String credentials = "client:secret";
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Basic " + encodedCredentials);

        HttpEntity<String> request = new HttpEntity<String>(headers);

        String access_token_url = "http://localhost:8080/oauth/token";
        access_token_url += "?client_id=" + "client";
        access_token_url += "?secret_id=" + "secret";
        access_token_url += "?username=" + "toto2@gmail.Com";
        access_token_url += "?password=" + "toto1234";
        access_token_url += "&grant_type=password";

        ResponseEntity<String> exchange = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);

        System.out.println("Access Token Response ---------" + exchange.getBody());
*/

        return true;
    }
}
