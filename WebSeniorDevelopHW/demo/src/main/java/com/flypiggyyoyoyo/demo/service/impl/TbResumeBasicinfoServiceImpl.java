package com.flypiggyyoyoyo.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flypiggyyoyoyo.demo.model.TbResumeBasicinfo;
import com.flypiggyyoyoyo.demo.service.TbResumeBasicinfoService;
import com.flypiggyyoyoyo.demo.mapper.TbResumeBasicinfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
* @author flypiggy
* @description 针对表【tb_resume_basicinfo】的数据库操作Service实现
* @createDate 2025-06-12 17:08:05
*/
@Service
public class TbResumeBasicinfoServiceImpl extends ServiceImpl<TbResumeBasicinfoMapper, TbResumeBasicinfo>
    implements TbResumeBasicinfoService{

    @Override
    public IPage<TbResumeBasicinfo> pageQuery(Page<TbResumeBasicinfo> page, String jobExperience, String keyword) {
        QueryWrapper<TbResumeBasicinfo> qw = new QueryWrapper<>();
        if (StringUtils.hasText(jobExperience)) {
            qw.eq("job_experience", jobExperience);
        }
        if (StringUtils.hasText(keyword)) {
            // 同时匹配姓名或求职意向
            qw.and(w -> w
                    .like("realname", keyword)
                    .or()
                    .like("job_intension", keyword)
            );
        }
        // 按最新编辑（ID）倒序
        qw.orderByDesc("basicinfo_id");
        return baseMapper.selectPage(page, qw);
    }
}




