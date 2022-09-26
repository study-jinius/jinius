package com.study.jinius.account.service;

import com.study.jinius.account.model.*;
import com.study.jinius.account.repository.AccountRepository;
import com.study.jinius.common.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


// TODO: 계층 고민하기
@Service
@RequiredArgsConstructor
public class AccountService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public AccountSignUpResponse signUp(AccountSignUpParam param) {
        String encodedPassword = passwordEncoder.encode(param.getPassword());
        param.setPassword(encodedPassword);

        Account account = param.toAccount();
        Account result = accountRepository.save(account);

        return AccountSignUpResponse.from(result);
    }

    public AccountSignInResponse signIn(AccountSignInParam param) {
        // AuthenticationManager는 authenticate()를 통해 시큐리티에 구현된 인증 절차를 진행한다.
        Account account = accountRepository.findByStringId(param.getStringId()).orElseThrow();

        if (!passwordEncoder.matches(param.getPassword(), account.getPassword())) {
            // TODO: 예외처리
        }

        Authentication authentication
                = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(param.getStringId(), param.getPassword()));

        String token = jwtTokenProvider.generateToken(authentication);
        return AccountSignInResponse.from(token);
    }


}
