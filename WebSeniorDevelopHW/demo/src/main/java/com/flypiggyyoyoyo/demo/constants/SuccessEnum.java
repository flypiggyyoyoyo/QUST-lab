package com.flypiggyyoyoyo.demo.constants;

import lombok.Getter;

@Getter
public enum SuccessEnum {
    LOGIN_SUCCESS(200, "登录成功"),
    REGISTER_SUCCESS(201, "注册成功");

    private final int code;
    private final String message;

    private SuccessEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
