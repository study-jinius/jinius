package com.study.jinius.account.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountSignInRequest {
    private String email;
    private String password;

    public AccountSignInParam toParam() {
        AccountSignInParam param = new AccountSignInParam();
        param.setEmail(email);
        param.setPassword(password);

        return param;
    }
}
