package com.flypiggyyoyoyo.backend.constants;

import lombok.Getter;

@Getter
public enum ErrorEnum {
    SUCCESS(200, "OK"),
    REGISTER_ERROR(40001, "注册失败，用户已存在"),
    LOGIN_USER_NOT_FOUND(40101, "登录失败，用户不存在"),
    LOGIN_PASSWORD_ERROR(40102, "登录失败，密码错误"),
    ILLEGAL_SOURCE(40301, "非法请求来源"),
    DATABASE_ERROR(50001, "数据库操作失败"),

    // Todo相关错误（按404xx和500xx格式）
    INVALID_STATUS(400, "无效的状态值"),
    TODO_NOT_FOUND(40401, "待办事项不存在", 404),
    TODO_OPERATION_FAILED(50002, "待办事项操作失败"),
    TODO_PERMISSION_DENIED(40302, "无权限操作该待办事项", 403);

    private final int code;
    private final String message;
    private final int httpStatus;

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;

        // 根据错误码范围设置默认HTTP状态码
        if (code >= 40000 && code < 50000) {
            this.httpStatus = 400;
        } else if (code >= 50000) {
            this.httpStatus = 500;
        } else {
            this.httpStatus = 200;
        }
    }

    ErrorEnum(int code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}