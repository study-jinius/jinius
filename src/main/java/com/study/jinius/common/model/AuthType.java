package com.study.jinius.common.model;

import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public enum AuthType {
    EMAIL,
    GOOGLE,
    NAVER,
}
