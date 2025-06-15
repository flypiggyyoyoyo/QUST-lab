package com.flypiggyyoyoyo.demo.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flypiggyyoyoyo.demo.constants.ErrorEnum;
import com.flypiggyyoyoyo.demo.constants.SuccessEnum;
import com.flypiggyyoyoyo.demo.data.user.login.UserLoginRequest;
import com.flypiggyyoyoyo.demo.data.user.login.UserLoginResponse;
import com.flypiggyyoyoyo.demo.data.user.register.UserRegisterRequest;
import com.flypiggyyoyoyo.demo.data.user.register.UserRegisterResponse;
import com.flypiggyyoyoyo.demo.exception.DatabaseException;
import com.flypiggyyoyoyo.demo.exception.UserException;
import com.flypiggyyoyoyo.demo.model.TbUsers;
import com.flypiggyyoyoyo.demo.service.TbUsersService;
import com.flypiggyyoyoyo.demo.mapper.TbUsersMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
* @author flypiggy
* @description 针对表【tb_users】的数据库操作Service实现
* @createDate 2025-06-12 17:08:13
*/
@Service
public class TbUsersServiceImpl extends ServiceImpl<TbUsersMapper, TbUsers>
    implements TbUsersService{

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        UserLoginResponse response = new UserLoginResponse();

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
        response.setCode(SuccessEnum.LOGIN_SUCCESS.getCode());
        response.setUserName(request.getUserName());

        return response;
    }

    @Override
    public UserRegisterResponse register(UserRegisterRequest request) {
        String logName = request.getUserLogname();
        String password = request.getUserPwd();
        String email = request.getUserEmail();
        String realName = request.getUserRealname();
        int role = request.getUserRole();
        int state = request.getUserState();

        // 验证：邮箱
        if(isRegister(email)){
            throw new UserException(ErrorEnum.EMAIL_EXIST);
        }

        // 注册流程

        Snowflake snowflake = new Snowflake(1, 1);
        String encryptedPassword= DigestUtils.md5DigestAsHex(password.getBytes());

        TbUsers tbUsers = new TbUsers()
                .setUserLogname(logName)
                .setUserPwd(encryptedPassword)
                .setUserEmail(email)
                .setUserRealname(realName)
                .setUserRole(role)
                .setUserState(state);

        boolean isUserSave = this.save(tbUsers);

        if(!isUserSave) {
            throw new DatabaseException("数据库异常，存储信息失败");
        }

        UserRegisterResponse response = new UserRegisterResponse();
        response.setCode(SuccessEnum.REGISTER_SUCCESS.getCode());

        return response;
    }

    private boolean isRegister (String email){
        QueryWrapper<TbUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_EMAIL", email);
        long count = this.count(queryWrapper);
        return count > 0;
    }
}




