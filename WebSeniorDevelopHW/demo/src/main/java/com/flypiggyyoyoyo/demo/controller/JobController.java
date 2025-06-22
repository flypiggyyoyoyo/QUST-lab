package com.flypiggyyoyoyo.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.flypiggyyoyoyo.demo.model.TbJob;
import com.flypiggyyoyoyo.demo.service.TbJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/job")
public class JobController {

    @Autowired
    private TbJobService jobService;

    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String jobNameFilter,
            @RequestParam(required = false) Long companyIdFilter,
            Model model) {

        QueryWrapper<TbJob> wrapper = new QueryWrapper<>();
        // 如果需要按 jobName 模糊搜索
        if (jobNameFilter != null && !jobNameFilter.isEmpty()) {
            // 注意：XML 中用别名 j，但 Wrapper 拼接时 MyBatis-Plus 最终会替换到 SQL，通常直接用列名即可
            wrapper.like("JOB_NAME", jobNameFilter);
        }
        // 如果需要按 companyId 过滤
        if (companyIdFilter != null) {
            wrapper.eq("COMPANY_ID", companyIdFilter);
        }
        // 可继续添加更多过滤条件

        IPage<TbJob> pageData = jobService.getJobPageWithCompany(page, size, wrapper);
        model.addAttribute("page", pageData);
        // 同时可把过滤条件回显
        model.addAttribute("jobNameFilter", jobNameFilter);
        model.addAttribute("companyIdFilter", companyIdFilter);
        return "manage/jobList"; // 对应 Thymeleaf 模板路径 templates/job/list.html
    }

}
