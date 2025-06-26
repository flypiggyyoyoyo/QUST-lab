package com.flypiggyyoyoyo.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flypiggyyoyoyo.demo.model.TbJob;
import com.flypiggyyoyoyo.demo.service.TbJobService;
import com.flypiggyyoyoyo.demo.mapper.TbJobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
* @author flypiggy
* @description 针对表【tb_job】的数据库操作Service实现
* @createDate 2025-06-12 17:07:48
*/
@Service
public class TbJobServiceImpl extends ServiceImpl<TbJobMapper, TbJob>
        implements TbJobService {

    @Autowired
    private TbJobMapper jobMapper;

    @Override
    public IPage<TbJob> getJobPageWithCompany(int pageNum, int pageSize, QueryWrapper<TbJob> wrapper) {
        if (wrapper == null) {
            wrapper = new QueryWrapper<>();
        }
        Page<TbJob> page = new Page<>(pageNum, pageSize);
        return baseMapper.selectJobPage(page, wrapper);
    }

    @Override
    public boolean deleteJobById(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean updateJob(TbJob job) {
        return this.updateById(job);
    }

    @Override
    public List<TbJob> getAllJobs() {
        return this.list();
    }

    @Override
    public int deleteInvalidJobs() {
        QueryWrapper<TbJob> wrapper = new QueryWrapper<>();
        wrapper.lt("job_endtime", new Date());
        wrapper.notInSql("job_id", "SELECT job_id FROM tb_jobapply");
        return jobMapper.delete(wrapper);
    }

}





