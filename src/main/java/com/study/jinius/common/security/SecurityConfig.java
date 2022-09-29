package com.study.jinius.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 웹 보안 활성화를 위한 annotation
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 어노테이션을 메소드 단위로 추가하기 위해 적용
public class SecurityConfig {
    // WebSecurityConfigureAdapter Deprecated
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 스프링 시큐리티 설정
        // http 요청에 대한 웹 기반 보안 구성
        http
                .csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .authorizeRequests()
                .antMatchers("/swagger-ui/**", "/api-docs/**").permitAll()
                .antMatchers("/api/accounts/**").permitAll()
                .anyRequest().authenticated()

                // JWT를 쓰려면 Spring Security에서 기본적으로 지원하는 Session 설정을 해제해야 한다.
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // JWT 적용
                .and()
                .apply(new JwtSecurityConfig(jwtTokenProvider));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        // AuthenticationManager는 스프링 시큐리티에서 인증을 담당한다.
        return authenticationConfiguration.getAuthenticationManager();
    }

//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() throws Exception {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setSigningKey("TEMP_KEY");
//        converter.afterPropertiesSet();
//        return converter;
//    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        // 모든 웹 보안에 전역적으로 영향을 미치는 항목 구성
//        // 아직 뭔지 잘 모르겠기 때문에 주석처리
//        // filterChain에서도 auth.antMatchers("/api/posts/*").permitAll(); 처럼 설정 가능.
//        return web -> web
//                .ignoring().antMatchers("/swagger-ui/**", "/api/accounts/sign-up");
//    }
}
