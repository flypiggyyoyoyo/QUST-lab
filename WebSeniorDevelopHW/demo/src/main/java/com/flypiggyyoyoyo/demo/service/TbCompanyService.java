package com.flypiggyyoyoyo.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.flypiggyyoyoyo.demo.data.company.register.CompanyRegisterRequest;
import com.flypiggyyoyoyo.demo.data.company.register.CompanyRegisterResponse;
import com.flypiggyyoyoyo.demo.data.company.update.CompanyUpdateRequest;
import com.flypiggyyoyoyo.demo.model.TbCompany;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author flypiggy
* @description 针对表【tb_company】的数据库操作Service
* @createDate 2025-06-12 17:07:38
*/
public interface TbCompanyService extends IService<TbCompany> {
    CompanyRegisterResponse register(CompanyRegisterRequest request);

    /**
     * 分页查询企业列表
     * @param pageNum 当前页码
     * @param pageSize 每页数量
     * @param name 企业名称（模糊查询）
     * @param size 企业规模
     * @return 分页结果
     */
    IPage<TbCompany> getCompanyPage(int pageNum, int pageSize, String name, String size);

    void updateCompany(CompanyUpdateRequest req);
}
