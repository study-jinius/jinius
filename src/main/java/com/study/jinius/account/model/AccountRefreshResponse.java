package com.study.jinius.account.model;

import com.study.jinius.common.security.JwtToken;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountRefreshResponse {
    private String accessToken;

    public static AccountRefreshResponse from(JwtToken token) {
        return new AccountRefreshResponse(token.getAccess());
    }
}
