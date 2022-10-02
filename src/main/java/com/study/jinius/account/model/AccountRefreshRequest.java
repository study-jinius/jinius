package com.study.jinius.account.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountRefreshRequest {
    private String token;

    public AccountRefreshCond toCond() {
        AccountRefreshCond cond = new AccountRefreshCond();
        cond.setRefreshToken(this.token);

        return cond;
    }
}
