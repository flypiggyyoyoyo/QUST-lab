package com.flypiggyyoyoyo.demo.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flypiggyyoyoyo.demo.constants.ErrorEnum;
import com.flypiggyyoyoyo.demo.constants.SuccessEnum;
import com.flypiggyyoyoyo.demo.data.user.login.UserLoginRequest;
import com.flypiggyyoyoyo.demo.data.user.login.UserLoginResponse;
import com.flypiggyyoyoyo.demo.data.user.register.UserRegisterRequest;
import com.flypiggyyoyoyo.demo.data.user.register.UserRegisterResponse;
import com.flypiggyyoyoyo.demo.data.user.update.UserUpdateRequest;
import com.flypiggyyoyoyo.demo.exception.DatabaseException;
import com.flypiggyyoyoyo.demo.exception.UserException;
import com.flypiggyyoyoyo.demo.model.TbUsers;
import com.flypiggyyoyoyo.demo.service.TbUsersService;
import com.flypiggyyoyoyo.demo.mapper.TbUsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
* @author flypiggy
* @description 针对表【tb_users】的数据库操作Service实现
* @createDate 2025-06-12 17:08:13
*/
@Service
public class TbUsersServiceImpl extends ServiceImpl<TbUsersMapper, TbUsers>
    implements TbUsersService{

    @Autowired
    private TbUsersMapper tbUsersMapper;

    @Override
    public UserLoginResponse login(UserLoginRequest request, HttpServletRequest servletRequest) {
        QueryWrapper<TbUsers> wrapper = new QueryWrapper<>();
        wrapper.eq("USER_LOGNAME", request.getUserName());

        TbUsers user = this.getOnly(wrapper,true);

        // 用户是否已经注册
        if (user == null) {
            throw new UserException(ErrorEnum.USER_NOT_EXIST);
        }

        // 验证密码
        String encryptedPassword= DigestUtils.md5DigestAsHex(request.getPassword().getBytes());
        if(!user.getUserPwd().equals(encryptedPassword)){
            throw new UserException(ErrorEnum.PASSWORD_ERROR);
        }

        // 1. 从Session中获取存储的验证码
        String storedCode = (String) servletRequest.getSession().getAttribute("validateCode");

        // 2. 获取用户输入的验证码
        String userInputCode = request.getCode();

        // 3. 验证验证码（忽略大小写）
        if (storedCode == null || !storedCode.equalsIgnoreCase(userInputCode)) {
            throw new UserException(ErrorEnum.CAPTCHA_ERROR);
        }

        // 4. 验证通过后，删除Session中的验证码（防止重复使用）
        servletRequest.getSession().removeAttribute("validateCode");

        UserLoginResponse response = new UserLoginResponse();

        response.setUserName(request.getUserName())
                .setUserId(user.getUserId())
                .setRole(user.getUserRole())
                .setCode(SuccessEnum.LOGIN_SUCCESS.getCode());

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

    @Override
    public Page<TbUsers> findByRoleAndKeyword(Page<TbUsers> page, String role, String keyword) {
        QueryWrapper<TbUsers> wrapper = new QueryWrapper<>();

        // 角色过滤
        if (StringUtils.hasText(role)) {
            wrapper.eq("user_role", role);  // 如果传入了 role，就加上这个过滤条件
        }

        // 关键字过滤：login_name 或 real_name 模糊匹配
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                    .like("user_logname", keyword)  // 使用 user_logname 作为字段
                    .or()
                    .like("user_realname", keyword) // 使用 user_realname 作为字段
            );
        }

        // 排序字段
        wrapper.orderByDesc("user_id"); // 指定用 user_id 排序

        // 调用 MyBatis-Plus 内置分页查询
        return this.page(page, wrapper);
    }

    @Override
    public void updateUser(UserUpdateRequest req) {
        // 检查userId是否存在
        if (req.getUserId() == 0) {
            throw new UserException("用户ID不能为空！");
        }

        // 使用userId查询用户
        TbUsers user = tbUsersMapper.selectById(req.getUserId());

        if (user != null) {
            // 更新用户信息
            user.setUserRealname(req.getRealName());
            user.setUserEmail(req.getEmail());
            user.setUserRole(req.getRole());
            user.setUserState(req.getState());

            // 如果修改了登录名，也需要更新
            if (req.getLogName() != null && !req.getLogName().equals(user.getUserLogname())) {
                user.setUserLogname(req.getLogName());
            }

            // 执行更新操作
            int updateCount = tbUsersMapper.updateById(user);
            if (updateCount == 0) {
                throw new UserException("用户更新失败，请稍后再试！");
            }
        } else {
            throw new UserException("用户未找到，无法更新！");
        }
    }


    private boolean isRegister (String email){
        QueryWrapper<TbUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_EMAIL", email);
        long count = this.count(queryWrapper);
        return count > 0;
    }
}




