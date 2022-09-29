package com.study.jinius.common.exception;

/**
 * 등록할 데이터가 이미 존재하는 경우
 */
public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException() {
        super();
    }

    public AlreadyExistsException(String s) {
        super(s);
    }

    public AlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
