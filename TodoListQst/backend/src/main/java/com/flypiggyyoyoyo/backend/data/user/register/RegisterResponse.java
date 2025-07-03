package com.flypiggyyoyoyo.backend.data.user.register;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)

public class RegisterResponse {
    private String username;
}
