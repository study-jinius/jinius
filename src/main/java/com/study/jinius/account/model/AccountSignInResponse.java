package com.study.jinius.account.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AccountSignInResponse {
    private String token;

    public static AccountSignInResponse from(String token) {
        return new AccountSignInResponse(token);
    }
}
