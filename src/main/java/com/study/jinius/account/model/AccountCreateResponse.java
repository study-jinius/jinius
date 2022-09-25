package com.study.jinius.account.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCreateResponse {
    private String stringId;
    private String name;

    public static AccountCreateResponse from(Account account) {
        AccountCreateResponse response = new AccountCreateResponse();
        response.setStringId(account.getStringId());
        response.setName(account.getName());

        return response;
    }
}
