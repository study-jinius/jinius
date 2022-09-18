package com.study.jinius.attachment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Result {
    SUCCESS("성공"),
    UNSUPPORTED("지원하지 않는 형식"),
    EMPTY("빈 파일");

    private final String message;
}
