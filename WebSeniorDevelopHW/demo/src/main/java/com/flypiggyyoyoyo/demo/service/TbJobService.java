package com.flypiggyyoyoyo.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.flypiggyyoyoyo.demo.model.TbJob;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.format.annotation.DateTimeFormat;

/**
* @author flypiggy
* @description 针对表【tb_job】的数据库操作Service
* @createDate 2025-06-12 17:07:48
*/
public interface TbJobService extends IService<TbJob> {
    /**
     * 分页查询 Job，并关联 companyName
     */
    IPage<TbJob> getJobPageWithCompany(int pageNum, int pageSize, QueryWrapper<TbJob> wrapper);

    public boolean deleteJobById(Long id);

    boolean updateJob(TbJob job);
}
