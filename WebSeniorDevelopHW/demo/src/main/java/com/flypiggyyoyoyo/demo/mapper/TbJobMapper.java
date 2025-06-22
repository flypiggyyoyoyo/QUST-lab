package com.flypiggyyoyoyo.demo.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flypiggyyoyoyo.demo.model.TbJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author flypiggy
* @description 针对表【tb_job】的数据库操作Mapper
* @createDate 2025-06-12 17:07:48
* @Entity com.flypiggyyoyoyo.demo.model.TbJob
*/

@Mapper
public interface TbJobMapper extends BaseMapper<TbJob> {
    /**
     * 自定义分页查询，联表查询 companyName
     * @param page MyBatis-Plus 分页对象
     * @param wrapper 动态条件，若无可传 new QueryWrapper<>()
     * @return 包含 companyName 字段的 TbJob 分页结果
     */
    IPage<TbJob> selectJobPage(Page<?> page, @Param(Constants.WRAPPER) Wrapper<TbJob> wrapper);
}



