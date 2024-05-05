package com.ashish.Authservice.configuration.Oauth;

import com.ashish.Authservice.configuration.Oauth.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

public class GoogleOpaqueTokenIntrospector implements OpaqueTokenIntrospector {
    @Autowired
    private  WebClient userInfoClient;

    public GoogleOpaqueTokenIntrospector(WebClient userInfoClient) {
        this.userInfoClient = userInfoClient;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        UserInfo userInfo = userInfoClient.get()
                .uri( uriBuilder -> uriBuilder
                        .path("/oauth2/v3/userinfo")
                        .queryParam("access_token", token)
                        .build())
                .retrieve()
                .bodyToMono(UserInfo.class)
                .block();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", userInfo.getSub());
        attributes.put("name", userInfo.getName());
        return new OAuth2IntrospectionAuthenticatedPrincipal(userInfo.getName(), attributes, null);
    }
}
