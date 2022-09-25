package com.study.jinius.account.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCreateParam {
    private String stringId;
    private String password;
    private String name;

    public Account toAccount() {
        return new Account(stringId, password, name);
    }
}
