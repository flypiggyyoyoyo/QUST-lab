package com.flypiggyyoyoyo.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flypiggyyoyoyo.demo.model.TbResumeBasicinfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author flypiggy
* @description 针对表【tb_resume_basicinfo】的数据库操作Service
* @createDate 2025-06-12 17:08:05
*/
public interface TbResumeBasicinfoService extends IService<TbResumeBasicinfo> {
    /**
     * 分页查询简历列表，支持按工作经验和关键词（姓名／求职意向）过滤
     *
     * @param page          MyBatis-Plus 分页对象
     * @param jobExperience 工作经验筛选（可选）
     * @param keyword       关键词，匹配 realname 或 job_intension（可选）
     * @return 分页结果
     */
    IPage<TbResumeBasicinfo> pageQuery(Page<TbResumeBasicinfo> page, String jobExperience, String keyword);
}
