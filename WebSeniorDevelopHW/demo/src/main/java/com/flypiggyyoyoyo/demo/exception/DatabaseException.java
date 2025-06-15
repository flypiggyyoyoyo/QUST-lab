package com.flypiggyyoyoyo.demo.exception;

import com.flypiggyyoyoyo.demo.constants.ErrorEnum;

/**
 * 数据库操作异常
 */
public class DatabaseException extends BaseBusinessException {
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public DatabaseException(ErrorEnum errorEnum, String message) {
        super(errorEnum, message);
    }

    @Override
    protected int getDefaultErrorCode() {
        return ErrorEnum.SERVER_ERROR.getCode();
    }
}