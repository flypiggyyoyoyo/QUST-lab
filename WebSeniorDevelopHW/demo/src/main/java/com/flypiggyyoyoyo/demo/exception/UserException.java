package com.flypiggyyoyoyo.demo.exception;

import com.flypiggyyoyoyo.demo.constants.ErrorEnum;

/**
 * 用户相关异常
 */
public class UserException extends BaseBusinessException {
    public UserException(String message) {
        super(message);
    }

    public UserException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public UserException(ErrorEnum errorEnum, String message) {
        super(errorEnum, message);
    }

    @Override
    protected int getDefaultErrorCode() {
        return ErrorEnum.USER_NOT_EXIST.getCode();
    }
}