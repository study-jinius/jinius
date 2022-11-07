package com.study.jinius.oauth.service;

import com.study.jinius.account.model.Account;
import com.study.jinius.account.model.Role;
import com.study.jinius.account.repository.AccountRepository;
import com.study.jinius.common.exception.UnknownException;
import com.study.jinius.common.model.AuthType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

import static com.study.jinius.common.model.AuthType.values;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    // 구글 로그인 작동 방식
    // 구글 로그인 버튼 클릭 > 구글로그인 > code 리턴(OAuth-Client Library) > AccessToken 요청
    // userRequest 정보 > loadUser함수 호출 > 구글로부터 회원 프로필을 받아줌

    // 구글로부터 받은 userRequest 데이터에 대한 후처리 함수
    // 함수 종료 시, @AuthenticationPrincipal 어노테이션이 만들어진다.
    // OAuth2 로그인 시 사용
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();                    // 프로필 정보
        String platform = userRequest.getClientRegistration().getRegistrationId();      // google, naver...

        signUpIfNotExists(platform, attributes);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(Role.USER.name())),
                oAuth2User.getAttributes(),
                userRequest
                        .getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUserNameAttributeName()
        );
    }

    private void signUpIfNotExists(String platform, Map<String, Object> attributes) {
        String email = attributes.get("email").toString();
        String name = attributes.get("name").toString();

        Account account = accountRepository.findByEmail(email).orElse(null);

        if (account == null) {
            // 회원 가입
            accountRepository.save(Account.builder()
                    .name(name)
                    .email(email)
                    .password(passwordEncoder.encode(email))
                    .role(Role.USER)
                    .authType(findAuthType(platform))
                    .build()
            );
        }
    }

    private AuthType findAuthType(String platform) {
        for (AuthType type : values()) {
            if (type.name().equals(StringUtils.upperCase(platform))) {
                return type;
            }
        }

        throw new UnknownException("존재하지 않는 인증 타입입니다.");
    }
}