package com.flypiggyyoyoyo.backend.data.user.login;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginResponse {
    private Integer userId;
    private String username;
    private String userEmail;
    private String accessToken;  // 访问令牌
    private String refreshToken; // 刷新令牌
    private Long expiresIn;      // 有效期（秒）
}
