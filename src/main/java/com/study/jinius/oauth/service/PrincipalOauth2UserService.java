package com.study.jinius.oauth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    // 구글 로그인 작동 방식
    // 구글 로그인 버튼 클릭 > 구글로그인 > code 리턴(OAuth-Client Library) > AccessToken 요청
    // userRequest 정보 > loadUser함수 호출 > 구글로부터 회원 프로필을 받아줌

    // 구글로부터 받은 userRequest 데이터에 대한 후처리 함수
    // 함수 종료 시, @AuthenticationPrincipal 어노테이션이 만들어진다.
    // OAuth2 로그인 시 사용
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("getClientRegistration: " + userRequest.getClientRegistration());      // registration: 어떤 매체에서 로그인했는지 확인 가능
        log.info("getAccessToken: " + userRequest.getAccessToken().getTokenValue());    // access token

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes: " + oAuth2User.getAttributes()); // 프로필 정보
        String platform = userRequest.getClientRegistration().getRegistrationId();       // google, naver...

        if (platform.equals("google")) {
            log.info("Google을 이용한 로그인");
        } else if (platform.equals("naver")) {
            log.info("naver를 이용한 로그인");
        } else {
           // TODO: 예외처리
        }

        return oAuth2User;
    }
}