package com.flypiggyyoyoyo.backend.data.user.login;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {

    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "密码不能为空")
    @Size(min = 6, max = 16)
    private String password;
}
