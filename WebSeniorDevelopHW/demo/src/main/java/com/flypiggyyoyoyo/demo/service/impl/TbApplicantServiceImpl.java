package com.flypiggyyoyoyo.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flypiggyyoyoyo.demo.model.TbApplicant;
import com.flypiggyyoyoyo.demo.service.TbApplicantService;
import com.flypiggyyoyoyo.demo.mapper.TbApplicantMapper;
import org.springframework.stereotype.Service;

/**
* @author flypiggy
* @description 针对表【tb_applicant】的数据库操作Service实现
* @createDate 2025-06-12 17:06:27
*/
@Service
public class TbApplicantServiceImpl extends ServiceImpl<TbApplicantMapper, TbApplicant>
    implements TbApplicantService{

    @Override
    public boolean existsById(Integer id) {
        return getById(id) != null;
    }

}




