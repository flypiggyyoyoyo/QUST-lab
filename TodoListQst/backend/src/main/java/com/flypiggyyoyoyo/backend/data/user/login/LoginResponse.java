package com.flypiggyyoyoyo.backend.data.user.login;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginResponse {
    private String username;
}
