package com.flypiggyyoyoyo.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flypiggyyoyoyo.demo.data.user.login.UserLoginRequest;
import com.flypiggyyoyoyo.demo.data.user.login.UserLoginResponse;
import com.flypiggyyoyoyo.demo.data.user.register.UserRegisterRequest;
import com.flypiggyyoyoyo.demo.data.user.register.UserRegisterResponse;
import com.flypiggyyoyoyo.demo.model.TbUsers;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.servlet.http.HttpServletRequest;

/**
* @author flypiggy
* @description 针对表【tb_users】的数据库操作Service
* @createDate 2025-06-12 17:08:13
*/

public interface TbUsersService extends IService<TbUsers> {
    default TbUsers getOnly(QueryWrapper<TbUsers> wrapper, boolean throwEx){

        // 在查询条件的末尾添加 "limit 1" 语句，确保查询结果最多只有一条记录
        wrapper.last("limit 1");

        return this.getOne(wrapper, throwEx);
    }

    UserLoginResponse login(UserLoginRequest requset, HttpServletRequest servletRequest);

    UserRegisterResponse register(UserRegisterRequest request);
}
