package com.flypiggyyoyoyo.demo.exception;

import com.flypiggyyoyoyo.demo.constants.ErrorEnum;

/**
 * 企业相关异常
 */
public class CompanyException extends BaseBusinessException {
    public CompanyException(String message) {
        super(message);
    }

    public CompanyException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public CompanyException(ErrorEnum errorEnum, String message) {
        super(errorEnum, message);
    }

    @Override
    protected int getDefaultErrorCode() {
        return ErrorEnum.ENTERPRISE_NOT_EXIST.getCode();
    }
}
