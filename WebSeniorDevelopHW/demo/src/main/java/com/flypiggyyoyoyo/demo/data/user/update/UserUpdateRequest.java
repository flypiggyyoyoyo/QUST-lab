package com.flypiggyyoyoyo.demo.data.user.update;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private int userId;
    private String logName;
    private String realName;
    private String email;
    private int role;
    private int state;
}
