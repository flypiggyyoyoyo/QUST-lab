package com.flypiggyyoyoyo.demo.exception;

import com.flypiggyyoyoyo.demo.constants.ErrorEnum;

/**
 * 基础业务异常类，封装公共异常处理逻辑
 */
public abstract class BaseBusinessException extends RuntimeException {
    private final int code;

    public BaseBusinessException(String message) {
        super(message);
        this.code = getDefaultErrorCode();
    }

    public BaseBusinessException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }

    public BaseBusinessException(ErrorEnum errorEnum, String message) {
        super(message);
        this.code = errorEnum.getCode();
    }

    public int getCode() {
        return code;
    }

    /**
     * 由子类实现，提供默认错误码
     */
    protected abstract int getDefaultErrorCode();
}