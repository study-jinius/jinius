package com.study.jinius.common.exception;

public class UnknownException extends RuntimeException {
    public UnknownException() {
        super();
    }

    public UnknownException(String s) {
        super(s);
    }

    public UnknownException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownException(Throwable cause) {
        super(cause);
    }
}
