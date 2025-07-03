package com.flypiggyyoyoyo.backend.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flypiggyyoyoyo.backend.constants.ErrorEnum;
import com.flypiggyyoyoyo.backend.data.user.login.LoginRequest;
import com.flypiggyyoyoyo.backend.data.user.login.LoginResponse;
import com.flypiggyyoyoyo.backend.data.user.register.RegisterRequest;
import com.flypiggyyoyoyo.backend.data.user.register.RegisterResponse;
import com.flypiggyyoyoyo.backend.exception.DatabaseException;
import com.flypiggyyoyoyo.backend.exception.UserException;
import com.flypiggyyoyoyo.backend.model.Users;
import com.flypiggyyoyoyo.backend.service.UsersService;
import com.flypiggyyoyoyo.backend.mapper.UsersMapper;
import com.flypiggyyoyoyo.backend.utils.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
* @author flypiggy
* @description 针对表【users】的数据库操作Service实现
* @createDate 2025-07-03 19:53:56
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        if (isRegister(username)) {
            throw new UserException(ErrorEnum.REGISTER_ERROR);
        }

        Snowflake snowflake = new Snowflake(1, 1);

        String encryptedPassword= DigestUtils.md5DigestAsHex(password.getBytes());

        Users user = new Users()
                .setUsername(username)
                .setPassword(encryptedPassword)
                .setEmail(request.getUserEmail());

        boolean isUserSave = this.save(user);

        if(!isUserSave) {
            throw new DatabaseException("数据库异常，存储信息失败");
        }

        return new RegisterResponse().setUsername(username);
    }

    private boolean isRegister(String username) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        long count = this.count(queryWrapper);
        return count > 0;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", request.getUsername());

        Users user = this.getOnly(queryWrapper, true);

        if (user == null) {
            throw new UserException(ErrorEnum.LOGIN_USER_NOT_FOUND);
        }

        String encryptedPassword = DigestUtils.md5DigestAsHex(request.getPassword().getBytes());
        if (!user.getPassword().equals(encryptedPassword)) {
            throw new UserException(ErrorEnum.LOGIN_PASSWORD_ERROR);
        }

        String accessToken = jwtUtils.generateAccessToken(user.getUserId(), user.getUsername());
        String refreshToken = jwtUtils.generateRefreshToken(user.getUserId(), user.getUsername());

        // 返回Token
        LoginResponse response = new LoginResponse();
        BeanUtils.copyProperties(user, response);
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);

        return response;
    }
}




