package com.study.jinius.account.model;

import com.study.jinius.common.model.AuthType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountSignUpParam {
    private String email;
    private String password;
    private String name;

    public Account toAccount() {
        return Account.builder()
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .role(Role.USER)
                .authType(AuthType.EMAIL)
                .build();
    }
}
