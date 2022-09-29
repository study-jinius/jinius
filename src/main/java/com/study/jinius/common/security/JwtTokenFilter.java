package com.study.jinius.common.security;

import com.study.jinius.common.exception.UnknownException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Request마다 1회만 실행되는 필터
 * 서버에서 읽고 유효한 토큰인지 판단
 */
public class JwtTokenFilter extends OncePerRequestFilter {
    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                // 정상 토큰이면 토큰을 통해 생성한 Authentication 객체(한 유저의 인증 정보를 가지고 있음)를 SecurityContext에 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw new UnknownException("토큰 검사 중 예외가 발생했습니다.", e);
        }

        filterChain.doFilter(request, response); // 다음 필터 체인 실행
    }
}
