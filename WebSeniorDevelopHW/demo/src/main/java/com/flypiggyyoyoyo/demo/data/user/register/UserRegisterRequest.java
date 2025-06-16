package com.flypiggyyoyoyo.demo.data.user.register;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserRegisterRequest {

    @NotEmpty(message = "名称不能为空")
    private String userLogname;

    @NotEmpty(message = "密码不能为空")
    private String userPwd;

    @NotEmpty(message = "真实姓名不能为空")
    private String userRealname;

    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String userEmail;

    @NotNull(message = "用户角色不能为空")
    private Integer userRole;

    @NotNull(message = "用户状态不能为空")
    private Integer userState;
}
