package com.study.jinius.account.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountSignUpParam {
    private String email;
    private String password;
    private String name;

    public Account toAccount() {
        return new Account(email, password, name);
    }
}
