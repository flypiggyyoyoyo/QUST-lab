package com.flypiggyyoyoyo.demo.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flypiggyyoyoyo.demo.model.TbJobapply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flypiggyyoyoyo.demo.model.vo.JobApplyView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author flypiggy
* @description 针对表【tb_jobapply】的数据库操作Mapper
* @createDate 2025-06-12 17:07:55
* @Entity com.flypiggyyoyoyo.demo.model.TbJobapply
*/

public interface TbJobapplyMapper extends BaseMapper<TbJobapply> {
    @Select("SELECT a.applicant_email, a.applicant_name, j.job_name, c.company_name, ja.apply_date, ja.apply_state " +
            "FROM tb_jobapply ja " +
            "JOIN tb_applicant a ON ja.applicant_id = a.applicant_id " +
            "JOIN tb_job j ON ja.job_id = j.job_id " +
            "JOIN tb_company c ON j.company_id = c.company_id")
    List<JobApplyView> getJobApplyViewList();

    @Select("SELECT a.applicant_email, a.applicant_name, j.job_name, c.company_name, ja.apply_date, ja.apply_state " +
            "FROM tb_jobapply ja " +
            "JOIN tb_applicant a ON ja.applicant_id = a.applicant_id " +
            "JOIN tb_job j ON ja.job_id = j.job_id " +
            "JOIN tb_company c ON j.company_id = c.company_id")
    List<JobApplyView> selectJobApplyViewPage(Page<JobApplyView> page, @Param("ew") Wrapper<JobApplyView> wrapper);
}




