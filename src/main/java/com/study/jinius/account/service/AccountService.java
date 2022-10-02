package com.study.jinius.account.service;

import com.study.jinius.account.model.*;
import com.study.jinius.account.repository.AccountRepository;
import com.study.jinius.common.exception.AlreadyExistsException;
import com.study.jinius.common.security.JwtToken;
import com.study.jinius.common.security.JwtTokenProvider;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


// TODO: 계층 고민하기
@Service
public class AccountService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final StatefulRedisConnection<String, String> statefulRedisConnection;
    private RedisCommands<String, String> commands;

    public AccountService(JwtTokenProvider jwtTokenProvider,
                          AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder,
                          AccountRepository accountRepository,
                          StatefulRedisConnection<String, String> statefulRedisConnection) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.statefulRedisConnection = statefulRedisConnection;
        this.commands = this.statefulRedisConnection.sync();
    }


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
        // AuthenticationManager의 구현체인 ProviderManager가 동작한다.
        Authentication authentication
                = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // 생성된 Authentication 객체는 SecurityContext에 저장된다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtToken token = jwtTokenProvider.generateToken(authentication);
        commands.set(param.getStringId(), token.getRefresh());
        return AccountSignInResponse.from(token);
    }

    public AccountRefreshResponse refresh(AccountRefreshCond cond) {
        String refreshToken = cond.getRefreshToken();
        jwtTokenProvider.validateToken(refreshToken);
        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);

        accountRepository.findByStringId(username)
                .filter(a -> !Role.NONE.equals(a.getRole())).orElseThrow();

        String storedToken = commands.get(username);
        if (!refreshToken.equals(storedToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        JwtToken token = jwtTokenProvider.reIssueToken(refreshToken);
        return AccountRefreshResponse.from(token);
    }
}
