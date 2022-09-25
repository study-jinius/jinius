package com.study.jinius.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 웹 보안 활성화를 위한 annotation
public class SecurityConfig {
    // WebSecurityConfigureAdapter Deprecated

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 스프링 시큐리티 설정
        // http 요청에 대한 웹 기반 보안 구성
        http.csrf().disable()
                .authorizeRequests()
                        .antMatchers("/swagger-ui/**", "/api-docs/**", "/api/accounts/sign-in").permitAll()
                        .anyRequest().authenticated()
                .and()
                .formLogin();

        return http.build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        // 모든 웹 보안에 전역적으로 영향을 미치는 항목 구성
//        // 아직 뭔지 잘 모르겠기 때문에 주석처리
//        // filterChain에서도 auth.antMatchers("/api/posts/*").permitAll(); 처럼 설정 가능.
//        return web -> web
//                .ignoring().antMatchers("/swagger-ui/**", "/api/accounts/sign-up");
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
