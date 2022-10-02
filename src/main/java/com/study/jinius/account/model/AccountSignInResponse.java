package com.study.jinius.account.model;

import com.study.jinius.common.security.JwtToken;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountSignInResponse {
    private String accessToken;
    private String refreshToken;

    public static AccountSignInResponse from(JwtToken token) {
        return new AccountSignInResponse(token.getAccess(), token.getRefresh());
    }
}
