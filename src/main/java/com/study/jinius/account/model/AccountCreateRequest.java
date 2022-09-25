package com.study.jinius.account.model;

import lombok.Getter;

@Getter
public class AccountCreateRequest {
    private String stringId;
    private String password;
    private String name;

    public AccountCreateParam toParam() {
        AccountCreateParam param = new AccountCreateParam();
        param.setStringId(stringId);
        param.setPassword(password);
        param.setName(name);

        return param;
    }
}
