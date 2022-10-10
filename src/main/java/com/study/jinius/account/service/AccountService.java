package com.study.jinius.account.service;

import com.study.jinius.account.model.*;
import com.study.jinius.account.repository.AccountRepository;
import com.study.jinius.common.exception.AlreadyExistsException;
import com.study.jinius.common.exception.BadRequestException;
import com.study.jinius.common.security.JwtToken;
import com.study.jinius.common.security.JwtTokenProvider;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


// TODO: 계층 고민하기
@Service
public class AccountService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
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
        this.commands = statefulRedisConnection.sync();
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

    public AccountSignInResponse signIn(AccountSignInParam param) throws BadRequestException {
        Account account = accountRepository.findByStringId(param.getStringId())
                .filter(a -> passwordEncoder.matches(param.getPassword(), a.getPassword()))
                .orElseThrow(() -> new BadRequestException("로그인 정보가 올바르지 않습니다."));

        JwtToken token = jwtTokenProvider.generateToken(String.valueOf(account.getIdx()));
        commands.setex(param.getStringId(), JwtTokenProvider.REFRESH_TOKEN_EXPIRED_TIME, token.getRefresh());
        return AccountSignInResponse.from(token);
    }

    public AccountRefreshResponse refresh(AccountRefreshCond cond) {
        String refreshToken = cond.getRefreshToken();
        jwtTokenProvider.validateToken(refreshToken);
        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);

        accountRepository.findById(Long.valueOf(username))
                .filter(a -> !Role.NONE.equals(a.getRole())).orElseThrow();

        String storedToken = commands.get(username);
        if (!refreshToken.equals(storedToken)) {
            throw new BadRequestException("유효하지 않은 토큰입니다.");
        }

        JwtToken token = jwtTokenProvider.reIssueToken(refreshToken);
        return AccountRefreshResponse.from(token);
    }
}
