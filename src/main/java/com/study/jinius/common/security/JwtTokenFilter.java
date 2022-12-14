package com.study.jinius.common.security;

import com.study.jinius.common.exception.UnknownException;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;


/**
 * Request마다 1회만 실행되는 필터
 * 서버에서 읽고 유효한 토큰인지 판단
 */
public class JwtTokenFilter extends OncePerRequestFilter {
    private JwtTokenProvider jwtTokenProvider;
    private static final Set<String> endPointSet = Set.of("/api/accounts/sign-in", "/api/accounts/refresh");

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
        // TODO: 나중에 해당 로직은 수정이 약간 필요해보임.
        if (token == null || !isCheckRequired(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwtTokenProvider.validateToken(token);

            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // 정상 토큰이면 토큰을 통해 생성한 Authentication 객체(한 유저의 인증 정보를 가지고 있음)를 SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            SecurityContextHolder.clearContext();
            throw e;
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw new UnknownException("토큰 검사 중 예외가 발생했습니다.", e);
        }

        filterChain.doFilter(request, response); // 다음 필터 체인 실행
    }

    private boolean isCheckRequired(String servletPath) {
        return !endPointSet.contains(servletPath);
    }
}
