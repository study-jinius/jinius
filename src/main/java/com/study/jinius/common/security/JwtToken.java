package com.study.jinius.common.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtToken {
    private String access;
    private String refresh;

    public JwtToken(String access) {
        this.access = access;
    }

    public JwtToken(String access, String refresh) {
        this.access = access;
        this.refresh = refresh;
    }
}
