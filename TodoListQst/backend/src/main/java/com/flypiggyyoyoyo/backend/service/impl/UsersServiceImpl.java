package com.flypiggyyoyoyo.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flypiggyyoyoyo.backend.model.Users;
import com.flypiggyyoyoyo.backend.service.UsersService;
import com.flypiggyyoyoyo.backend.mapper.UsersMapper;
import org.springframework.stereotype.Service;

/**
* @author flypiggy
* @description 针对表【users】的数据库操作Service实现
* @createDate 2025-07-03 19:53:56
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

}




