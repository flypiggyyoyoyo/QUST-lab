package com.flypiggyyoyoyo.demo.service;

import com.flypiggyyoyoyo.demo.data.company.register.CompanyRegisterRequest;
import com.flypiggyyoyoyo.demo.data.company.register.CompanyRegisterResponse;
import com.flypiggyyoyoyo.demo.model.TbCompany;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author flypiggy
* @description 针对表【tb_company】的数据库操作Service
* @createDate 2025-06-12 17:07:38
*/
public interface TbCompanyService extends IService<TbCompany> {
    CompanyRegisterResponse register(CompanyRegisterRequest request);
}
