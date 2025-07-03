package com.flypiggyyoyoyo.backend.exception;

import com.flypiggyyoyoyo.backend.constants.ErrorEnum;

public class TodoException extends RuntimeException {
    private final int code;

    public TodoException(String message) {
        super(message);
        this.code = ErrorEnum.DATABASE_ERROR.getCode();
    }

    public TodoException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }

    public TodoException(ErrorEnum errorEnum, String message) {
        super(message);
        this.code = errorEnum.getCode();
    }

    public int getCode() {
        return this.code;
    }
}