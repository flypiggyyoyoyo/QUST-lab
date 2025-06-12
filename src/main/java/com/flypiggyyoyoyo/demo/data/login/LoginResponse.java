package com.flypiggyyoyoyo.demo.data.login;

import com.flypiggyyoyoyo.demo.constants.ErrorEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginResponse {
    private int code;             // 状态码（使用枚举类中的 code）
    private String message;       // 状态消息（使用枚举类中的 message）

    private String userId;        // 用户ID
    private String userName;      // 用户名
    private String userEmail;     // 用户邮箱
    private String role;          // 角色

//    登录令牌
//    private String token;         // 访问令牌
//    private Long expireTime;      // 令牌过期时间（毫秒时间戳）

    // 快速构建成功响应
    public static LoginResponse success() {
        return new LoginResponse()
                .setCode(ErrorEnum.SUCCESS.getCode())
                .setMessage(ErrorEnum.SUCCESS.getMessage());
    }

    // 快速构建失败响应
    public static LoginResponse fail(ErrorEnum status) {
        return new LoginResponse()
                .setCode(status.getCode())
                .setMessage(status.getMessage());
    }
}
