package com.study.jinius.account.service;

import com.study.jinius.account.model.Account;
import com.study.jinius.account.model.Role;
import com.study.jinius.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String accountIdx) throws UsernameNotFoundException {
        // 내부적으로 provider에서 hideUserNotFoundException의 기본값은 false이기 때문에 메시지가 의미없다.
        Account account = accountRepository.findById(Long.valueOf(accountIdx))
                .filter(a -> !Role.NONE.equals(a.getRole()))
                .orElseThrow(() -> new UsernameNotFoundException(null));

        return User.builder()
                .username(String.valueOf(account.getIdx()))
                .password(account.getPassword())
                .authorities(account.getRole().name())
                .build();
    }

    public Long getAccountIdx() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
