package com.flypiggyyoyoyo.backend.constants;

import lombok.Getter;

@Getter
public enum ErrorEnum {
    SUCCESS(200, "OK"),
    REGISTER_ERROR(40001, "注册失败，用户已存在"),
    LOGIN_USER_NOT_FOUND(40101, "登录失败，用户不存在"),
    LOGIN_PASSWORD_ERROR(40102, "登录失败，密码错误"),
    ILLEGAL_SOURCE(40301, "非法请求来源"),
    DATABASE_ERROR(50001, "数据库操作失败");

    private final int code; // 业务错误码
    private final String message; // 错误信息
    private final int httpStatus; // HTTP状态码

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;

        // 根据错误类型设置默认HTTP状态码
        if (code >= 40000 && code < 50000) {
            this.httpStatus = 400; // 客户端错误
        } else if (code >= 50000) {
            this.httpStatus = 500; // 服务器错误
        } else {
            this.httpStatus = 200; // 成功
        }
    }

    ErrorEnum(int code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}