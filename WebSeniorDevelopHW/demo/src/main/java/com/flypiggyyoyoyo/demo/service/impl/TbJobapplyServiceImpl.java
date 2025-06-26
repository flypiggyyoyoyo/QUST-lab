package com.flypiggyyoyoyo.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flypiggyyoyoyo.demo.model.TbJobapply;
import com.flypiggyyoyoyo.demo.model.vo.JobApplyView;
import com.flypiggyyoyoyo.demo.service.TbJobapplyService;
import com.flypiggyyoyoyo.demo.mapper.TbJobapplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author flypiggy
* @description 针对表【tb_jobapply】的数据库操作Service实现
* @createDate 2025-06-12 17:07:55
*/
@Service
public class TbJobapplyServiceImpl extends ServiceImpl<TbJobapplyMapper, TbJobapply>
    implements TbJobapplyService{

    @Autowired
    private TbJobapplyMapper jobapplyMapper;

    @Override
    public IPage<TbJobapply> getApplicationPage(int page, int size, QueryWrapper<TbJobapply> wrapper) {
        Page<TbJobapply> pageObj = new Page<>(page, size);
        return this.page(pageObj, wrapper);
    }

    @Override
    public List<JobApplyView> getJobApplyViews(Page<JobApplyView> page, String applicantNameFilter, String jobNameFilter) {
        QueryWrapper<JobApplyView> wrapper = new QueryWrapper<>();
        if (applicantNameFilter != null && !applicantNameFilter.isEmpty()) {
            wrapper.like("a.applicant_name", applicantNameFilter);
        }
        if (jobNameFilter != null && !jobNameFilter.isEmpty()) {
            wrapper.like("j.job_name", jobNameFilter);
        }

        return jobapplyMapper.selectJobApplyViewPage(page, wrapper);
    }

}




