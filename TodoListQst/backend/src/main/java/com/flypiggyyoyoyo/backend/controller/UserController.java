package com.flypiggyyoyoyo.backend.controller;

import com.flypiggyyoyoyo.backend.common.Result;
import com.flypiggyyoyoyo.backend.data.user.login.LoginRequest;
import com.flypiggyyoyoyo.backend.data.user.login.LoginResponse;
import com.flypiggyyoyoyo.backend.data.user.register.RegisterRequest;
import com.flypiggyyoyoyo.backend.data.user.register.RegisterResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.flypiggyyoyoyo.backend.service.UsersService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UsersService usersService;

    @PostMapping("/register")
    public Result<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = usersService.register(request);
        return Result.OK(response);
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = usersService.login(request);
        return Result.OK(response);
    }
}
