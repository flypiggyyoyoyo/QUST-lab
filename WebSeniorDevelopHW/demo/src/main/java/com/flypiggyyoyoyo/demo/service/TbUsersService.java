package com.flypiggyyoyoyo.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 分页查询用户列表，支持按角色和关键字（用户名/真实姓名）筛选
     *
     * @param page     MyBatis-Plus 的分页对象，page.getCurrent()/page.getSize() 会被自动注入
     * @param role     角色（admin/hr/user），如果为 null 或空串则不做过滤
     * @param keyword  关键字，会同时匹配 loginName 或 realName；如果为 null 或空串则不做过滤
     * @return         分页后的用户列表
     */
    Page<TbUsers> findByRoleAndKeyword(Page<TbUsers> page, String role, String keyword);
}
