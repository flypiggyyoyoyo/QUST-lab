package com.flypiggyyoyoyo.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flypiggyyoyoyo.backend.data.user.login.LoginRequest;
import com.flypiggyyoyoyo.backend.data.user.login.LoginResponse;
import com.flypiggyyoyoyo.backend.data.user.register.RegisterRequest;
import com.flypiggyyoyoyo.backend.data.user.register.RegisterResponse;
import com.flypiggyyoyoyo.backend.model.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author flypiggy
* @description 针对表【users】的数据库操作Service
* @createDate 2025-07-03 19:53:56
*/
public interface UsersService extends IService<Users> {
    default Users getOnly(QueryWrapper<Users> wrapper, boolean throwEx){

        // 在查询条件的末尾添加 "limit 1" 语句，确保查询结果最多只有一条记录
        wrapper.last("limit 1");

        return this.getOne(wrapper, throwEx);
    }

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest requset);
}
