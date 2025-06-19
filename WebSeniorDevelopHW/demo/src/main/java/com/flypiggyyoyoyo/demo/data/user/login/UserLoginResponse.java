package com.flypiggyyoyoyo.demo.data.user.login;

import com.flypiggyyoyoyo.demo.constants.ErrorEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserLoginResponse {
    private int code;             // 状态码（使用枚举类中的 code）

    private String userRealName;
    private int userId;        // 用户ID
    private String userName;      // 用户名
    private String userEmail;     // 用户邮箱
    private int role;          // 角色

//    登录令牌
//    private String token;         // 访问令牌
//    private Long expireTime;      // 令牌过期时间（毫秒时间戳）

}
