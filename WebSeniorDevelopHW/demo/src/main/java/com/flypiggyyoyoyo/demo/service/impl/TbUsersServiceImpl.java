package com.flypiggyyoyoyo.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flypiggyyoyoyo.demo.constants.ErrorEnum;
import com.flypiggyyoyoyo.demo.data.login.LoginRequest;
import com.flypiggyyoyoyo.demo.data.login.LoginResponse;
import com.flypiggyyoyoyo.demo.model.TbUsers;
import com.flypiggyyoyoyo.demo.service.TbUsersService;
import com.flypiggyyoyoyo.demo.mapper.TbUsersMapper;
import org.springframework.stereotype.Service;

/**
* @author flypiggy
* @description 针对表【tb_users】的数据库操作Service实现
* @createDate 2025-06-12 17:08:13
*/
@Service
public class TbUsersServiceImpl extends ServiceImpl<TbUsersMapper, TbUsers>
    implements TbUsersService{

    @Override
    public LoginResponse login(LoginRequest request) {
        LoginResponse response = new LoginResponse();

        // TODO: 实现登录验证逻辑
        // 1. 根据用户名查询用户
        // TbUsers user = usersMapper.selectByUsername(request.getUsername());

        // 2. 验证用户是否存在
        // if (user == null) {
        //     response.setSuccess(false);
        //     response.setMessage("用户不存在");
        //     return response;
        // }

        // 3. 验证密码
        // if (!user.getPassword().equals(request.getPassword())) {
        //     response.setSuccess(false);
        //     response.setMessage("密码错误");
        //     return response;
        // }

        // 4. 验证验证码
        // String sessionCode = (String) request.getSession().getAttribute("captcha");
        // if (!sessionCode.equalsIgnoreCase(request.getValidateCode())) {
        //     response.setSuccess(false);
        //     response.setMessage("验证码错误");
        //     return response;
        // }

        // 5. 验证通过
        // response.setSuccess(true);
        // response.setUsername(user.getUsername());
        // response.setUserId(user.getId());

        // 模拟验证通过
        response.setCode(ErrorEnum.SUCCESS.getCode());
        response.setUserName(request.getUserName());

        return response;
    }
}




