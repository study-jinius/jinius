package com.study.jinius.account.model;

import lombok.Getter;

@Getter
public class AccountCreateRequest {
    private String email;
    private String password;
    private String name;

    public AccountSignUpParam toParam() {
        AccountSignUpParam param = new AccountSignUpParam();
        param.setEmail(email);
        param.setPassword(password);
        param.setName(name);

        return param;
    }
}
