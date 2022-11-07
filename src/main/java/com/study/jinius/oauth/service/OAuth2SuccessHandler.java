package com.study.jinius.oauth.service;

import com.study.jinius.account.model.Account;
import com.study.jinius.account.model.Role;
import com.study.jinius.account.repository.AccountRepository;
import com.study.jinius.common.exception.NotFoundException;
import com.study.jinius.common.exception.UnknownException;
import com.study.jinius.common.model.AuthType;
import com.study.jinius.common.security.JwtToken;
import com.study.jinius.common.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.study.jinius.common.model.AuthType.values;


@Slf4j
@Component
@AllArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private JwtTokenProvider jwtTokenProvider;
    private AccountRepository accountRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        log.info("oAuth2User = " + oAuth2User);

        String email = oAuth2User.getAttribute("email");

        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));
        JwtToken token = jwtTokenProvider.generateToken(String.valueOf(account.getIdx()));

        // TODO: 어디로 리다이렉트할지 결정 후 작업하기
        String targetUrl = UriComponentsBuilder.fromUriString("/")
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
