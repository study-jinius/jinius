package com.study.jinius.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;

@Service
public class SocialOAuthService {
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.naver.authorization-uri}")
    private String baseUrl;

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String tokenBaseUrl;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String callbackUrl;

    @Value("${spring.security.oauth2.client.registration.naver.authorization-grant-type}")
    private String grantType;

    public String getRequestLoginUrl() {
        final String state = new BigInteger(130, new SecureRandom()).toString();

        LinkedMultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
        requestParam.add("response_type", "code");
        requestParam.add("state", state);
        requestParam.add("client_id", clientId);
        requestParam.add("redirect_uri", callbackUrl);

        return UriComponentsBuilder.fromUriString(baseUrl)
                .queryParams(requestParam)
                .build().encode()
                .toString();
    }

    public ResponseEntity requestAccessToken(String code, String state) {
        LinkedMultiValueMap<Object, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", code);
        requestBody.add("state", state);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("grant_type", grantType);

        return new RestTemplate().postForEntity(tokenBaseUrl, requestBody, Map.class);
    }
}
