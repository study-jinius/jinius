package com.study.jinius.account.service;

import com.study.jinius.account.model.Account;
import com.study.jinius.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetailsService : 유저의 정보를 가져오는 인터페이스
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String stringId) throws UsernameNotFoundException {
        Account account = accountRepository.findByStringId(stringId).orElseThrow();

        return User.builder()
                .username(account.getStringId())
                .password(account.getPassword())
                .authorities(account.getRole().name())
                .build();
    }
}
