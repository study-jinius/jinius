package com.study.jinius.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class SocialOAuthController {

    private final SocialOAuthService socialOAuthService;

    @GetMapping("/naver")
    public void naverLogin(HttpServletRequest request, HttpServletResponse response) {
        String loginUrl = socialOAuthService.getRequestLoginUrl();

        try {
            response.sendRedirect(loginUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/naver/callback")
    public Object requestAccessCallback(@RequestParam(value = "code") String authCode, @RequestParam(value = "state") String state) {
        ResponseEntity responseEntity = socialOAuthService.requestAccessToken(authCode, state);
        Object responseMessage = responseEntity.getBody();

        return responseMessage;
    }

}
