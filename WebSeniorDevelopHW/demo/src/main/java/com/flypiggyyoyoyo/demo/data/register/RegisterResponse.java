package com.flypiggyyoyoyo.demo.data.register;

import com.flypiggyyoyoyo.demo.constants.ErrorEnum;
import jdk.vm.ci.code.Register;
import lombok.Data;

@Data
public class RegisterResponse {

    private int code;
    private String message;
    private Integer userId;
    private String userLogname;
    private String userRealname;
    private String userEmail;
    private Integer userRole;
    private Integer userState;

    // 成功响应构造函数
    public static RegisterResponse success(String message, Integer userId) {
        RegisterResponse response = new RegisterResponse();
        response.setCode(ErrorEnum.SUCCESS.getCode());
        response.setMessage(message);
        response.setUserId(userId);
        return response;
    }

    // 失败响应构造函数
    public static RegisterResponse fail(String message) {
        RegisterResponse response = new RegisterResponse();
        response.setCode(ErrorEnum.SUCCESS.getCode());
        response.setMessage(message);
        return response;
    }
}
