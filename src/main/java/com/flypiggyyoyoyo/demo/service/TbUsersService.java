package com.flypiggyyoyoyo.demo.service;

import com.flypiggyyoyoyo.demo.data.login.LoginRequest;
import com.flypiggyyoyoyo.demo.data.login.LoginResponse;
import com.flypiggyyoyoyo.demo.model.TbUsers;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
* @author flypiggy
* @description 针对表【tb_users】的数据库操作Service
* @createDate 2025-06-12 17:08:13
*/

public interface TbUsersService extends IService<TbUsers> {
    LoginResponse login(LoginRequest requset);
}
