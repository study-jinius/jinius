package com.study.jinius.attachment.exception;

/**
 * 지원하지 않는 형식의 파일 업로드 시도 시 발생하는 Exception
 */
public class UnsupportedFileException extends RuntimeException {
    public UnsupportedFileException(String message) {
        super(message);
    }
}
