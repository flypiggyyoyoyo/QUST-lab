package com.flypiggyyoyoyo.demo.data.login;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data

public class LoginRequest {

    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @NotEmpty(message = "密码不能为空")
    private String password;

    @NotEmpty(message = "验证码不能为空")
    private String code;
}
