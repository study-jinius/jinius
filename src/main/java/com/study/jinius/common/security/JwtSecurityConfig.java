package com.study.jinius.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfigurerAdapter를 확장.
 * JwtTokenProvider를 주입받음.
 * JwtFilter를 통해 Security filterchain에 filter를 추가 등록
 */
@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider);
        // UsernamePasswordAuthenticationFilter가 동작하여 아이디, 패스워드를 통해 인증 요청을 진행하기 전에
        // jwtTokenFilter를 추가하여 정상적으로 JWT 토큰을 통한 인증 방식이 작동하도록 했다.
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

}