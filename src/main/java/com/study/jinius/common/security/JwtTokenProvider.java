package com.study.jinius.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 토큰 생성 및 유효성 검사
 */
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private String secretKey = "SECRET_KEY";

    public static final Long ACCESS_TOKEN_EXPIRED_TIME = 30 * 60 * 1000L; // 30분
    public static final Long REFRESH_TOKEN_EXPIRED_TIME = 14 * 24 * 60 * 60 * 1000L; // 2주

    private final UserDetailsService userDetailsService;

    public JwtToken generateToken(Authentication authentication) {
        String username = authentication.getName();
        String refreshToken = getRefreshToken(username);
        String accessToken = getAccessToken(username);

        return new JwtToken(accessToken, refreshToken);
    }

    public JwtToken reIssueToken(String refreshToken) {
        String username = getUsernameFromToken(refreshToken);
        String accessToken = getRefreshToken(username);

        return new JwtToken(accessToken);
    }

    /**
     * 토큰으로부터 클레임을 만들고, 이를 통해 User 객체를 생성하여 Authentication 객체를 반환
     *
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        String username = getUsernameFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    /**
     * http 헤더로부터 bearer 토큰을 가져옴.
     *
     * @param req
     * @return
     */
    public String resolveToken(HttpServletRequest req) {
        String bearerString = "Bearer ";
        String bearerToken = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(bearerString)) {
            return bearerToken.substring(bearerString.length());
        }
        return null;
    }

    /**
     * 토큰을 검증
     *
     * @param token
     */
    public void validateToken(String token) {
        Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }

    /**
     * 토큰으로부터 사용자 아이디 가져옴
     *
     * @param token
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    private String getRefreshToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);
        Date now = new Date();
        Date refreshExp = new Date(now.getTime() );

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(refreshExp)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return refreshToken;
    }

    private String getAccessToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);
        Date now = new Date();
        Date accessExp = new Date(now.getTime() + ACCESS_TOKEN_EXPIRED_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(accessExp)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
