package com.flypiggyyoyoyo.backend.constants;


import lombok.Getter;

@Getter
public enum ErrorEnum {
    SUCCESS(200,"OK"),
    REGISTER_ERROR(40001,"注册失败，用户已存在"),
    Login_ERROR(40002,"登录失败，用户名或密码错误"),
    NO_USER_ERROR(40003,"用户不存在"),

    ILLEGAL_SOURCE(40301, "非法请求来源");

    private final int code;
    private final String message;

    private ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
