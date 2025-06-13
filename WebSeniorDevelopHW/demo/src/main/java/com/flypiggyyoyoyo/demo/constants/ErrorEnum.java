package com.flypiggyyoyoyo.demo.constants;

import lombok.Getter;

@Getter
public enum ErrorEnum {
    SUCCESS(200, "登录成功"),
    USER_NOT_EXIST(401, "用户不存在"),
    PASSWORD_ERROR(402, "密码错误"),
    CAPTCHA_ERROR(403, "验证码错误"),
    SERVER_ERROR(500, "服务器异常"),

    USERNAME_EXIST(1004, "用户名已存在"),
    EMAIL_EXIST(1005, "邮箱已被注册"),
    PHONE_EXIST(1006, "手机号已被注册");

    private final int code;
    private final String message;

    private ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
