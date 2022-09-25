package com.study.jinius.account.service;

import com.study.jinius.account.model.Account;
import com.study.jinius.account.model.AccountCreateParam;
import com.study.jinius.account.model.AccountCreateResponse;
import com.study.jinius.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * UserDetailsService : 유저의 정보를 가져오는 인터페이스
 */
// TODO: 계층 고민하기
@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public AccountCreateResponse create(AccountCreateParam param) {
        String encodedPassword = passwordEncoder.encode(param.getPassword());
        param.setPassword(encodedPassword);

        Account account = param.toAccount();
        Account result = accountRepository.save(account);

        return AccountCreateResponse.from(result);
    }

    @Override
    public UserDetails loadUserByUsername(String stringId) throws UsernameNotFoundException {
        Account account = accountRepository.findByStringId(stringId);
        if (account == null) return null;

        // UserDetails를 커스터마이징하는 쪽도 고려해보자.
        return User.builder()
                .username(account.getStringId())
                .password(account.getPassword())
                .authorities("DEFAULT")
                .build();
    }
}
