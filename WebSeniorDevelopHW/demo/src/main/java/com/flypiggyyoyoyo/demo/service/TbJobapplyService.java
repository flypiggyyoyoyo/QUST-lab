package com.flypiggyyoyoyo.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flypiggyyoyoyo.demo.model.TbJobapply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.flypiggyyoyoyo.demo.model.vo.JobApplyView;

import java.util.List;

/**
* @author flypiggy
* @description 针对表【tb_jobapply】的数据库操作Service
* @createDate 2025-06-12 17:07:55
*/
public interface TbJobapplyService extends IService<TbJobapply> {
    IPage<TbJobapply> getApplicationPage(int page, int size, QueryWrapper<TbJobapply> wrapper);

    List<JobApplyView> getJobApplyViews(Page<JobApplyView> page, String applicantNameFilter, String jobNameFilter);

}
