package com.flypiggyyoyoyo.demo.service;

import com.flypiggyyoyoyo.demo.data.user.login.UserLoginRequest;
import com.flypiggyyoyoyo.demo.data.user.login.UserLoginResponse;
import com.flypiggyyoyoyo.demo.data.user.register.UserRegisterRequest;
import com.flypiggyyoyoyo.demo.data.user.register.UserRegisterResponse;
import com.flypiggyyoyoyo.demo.model.TbUsers;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author flypiggy
* @description 针对表【tb_users】的数据库操作Service
* @createDate 2025-06-12 17:08:13
*/

public interface TbUsersService extends IService<TbUsers> {
    UserLoginResponse login(UserLoginRequest requset);

    UserRegisterResponse register(UserRegisterRequest request);
}
