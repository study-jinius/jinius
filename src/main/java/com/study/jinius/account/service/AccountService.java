package com.study.jinius.account.service;

import com.study.jinius.account.model.*;
import com.study.jinius.account.repository.AccountRepository;
import com.study.jinius.common.exception.AlreadyExistsException;
import com.study.jinius.common.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        if (accountRepository.findByStringId(param.getStringId()).orElse(null) != null) {
            throw new AlreadyExistsException("이미 존재하는 회원입니다.");
        }

        String encodedPassword = passwordEncoder.encode(param.getPassword());
        param.setPassword(encodedPassword);

        Account account = param.toAccount();
        Account result = accountRepository.save(account);

        return AccountSignUpResponse.from(result);
    }

    public AccountSignInResponse signIn(AccountSignInParam param) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(param.getStringId(), param.getPassword());

        // 이 시점에 loadUserByUsername 메소드가 실행된다.
        Authentication authentication
                = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // 생성된 Authentication 객체는 SecurityContext에 저장된다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        return AccountSignInResponse.from(token);
    }
}
