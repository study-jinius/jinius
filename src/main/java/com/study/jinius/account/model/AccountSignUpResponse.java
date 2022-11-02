package com.study.jinius.account.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountSignUpResponse {
    private String email;
    private String name;

    public static AccountSignUpResponse from(Account account) {
        AccountSignUpResponse response = new AccountSignUpResponse();
        response.setEmail(account.getEmail());
        response.setName(account.getName());

        return response;
    }
}
