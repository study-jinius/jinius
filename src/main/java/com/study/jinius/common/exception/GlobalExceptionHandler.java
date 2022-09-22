package com.study.jinius.common.exception;

import com.study.jinius.common.model.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = IllegalArgumentException.class)
    public CommonResponse<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new CommonResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
