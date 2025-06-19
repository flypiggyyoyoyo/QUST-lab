package com.flypiggyyoyoyo.demo.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.servlet.http.HttpSession;

@Data
@Accessors(chain = true)
public class OnlineUser {
    private Long userId;
    private String userLogname;
    private String userRealname;
    private String userRole;
    private HttpSession session;

    public OnlineUser(Long userId, String userLogname, String userRealname, String userRole, HttpSession session) {
        this.userId = userId;
        this.userLogname = userLogname;
        this.userRealname = userRealname;
        this.userRole = userRole;
        this.session = session;
    }
}
