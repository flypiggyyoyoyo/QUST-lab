package com.flypiggyyoyoyo.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.flypiggyyoyoyo.demo.model.TbCompany;
import com.flypiggyyoyoyo.demo.model.TbJob;
import com.flypiggyyoyoyo.demo.model.TbJobapply;
import com.flypiggyyoyoyo.demo.service.TbCompanyService;
import com.flypiggyyoyoyo.demo.service.TbJobService;
import com.flypiggyyoyoyo.demo.service.TbJobapplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/job")
public class JobController {

    @Autowired
    private TbJobService jobService;

    @Autowired
    private TbCompanyService companyService;

    @Autowired
    private TbJobapplyService jobApplicationService;

    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String jobNameFilter,
            @RequestParam(required = false) Long companyIdFilter,
            Model model) {

        QueryWrapper<TbJob> wrapper = new QueryWrapper<>();
        if (jobNameFilter != null && !jobNameFilter.isEmpty()) {
            wrapper.like("JOB_NAME", jobNameFilter);
        }
        if (companyIdFilter != null) {
            wrapper.eq("COMPANY_ID", companyIdFilter);
        }
        IPage<TbJob> pageData = jobService.getJobPageWithCompany(page, size, wrapper);
        model.addAttribute("page", pageData);
        model.addAttribute("jobNameFilter", jobNameFilter);
        model.addAttribute("companyIdFilter", companyIdFilter);
        return "manage/jobList";
    }

    /**
     * 显示添加职位的表单
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        // 为表单绑定一个空的 TbJob 对象
        model.addAttribute("job", new TbJob());
        // 获取所有公司列表，用于下拉选择
        List<TbCompany> companies = companyService.getAllCompanies();
        model.addAttribute("companies", companies);
        // 返回添加职位的模板
        return "manage/jobAdd";
    }

    /**
     * 处理添加职位的表单提交
     */
    @PostMapping("/add")
    public String addJob(
            @Valid @ModelAttribute("job") TbJob job,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("companies", companyService.getAllCompanies());
            return "manage/jobAdd";
        }
        jobService.save(job);
        return "redirect:/job/list";
    }

    /**
     * 删除职位
     */
    @PostMapping("/delete")
    public String deleteJob(
            @RequestParam("jobId") Long jobId,
            RedirectAttributes redirectAttrs) {

        boolean ok = jobService.deleteJobById(jobId);
        if (ok) {
            redirectAttrs.addFlashAttribute("successMessage", "ID=" + jobId + " 的职位已删除");
        } else {
            redirectAttrs.addFlashAttribute("errorMessage", "删除失败：可能该职位不存在或存在关联数据");
        }
        return "redirect:/job/list";
    }

    /**
     * 显示“编辑职位”表单
     */
    @GetMapping("/edit")
    public String editForm(@RequestParam("jobId") Long jobId, Model model) {
        TbJob job = jobService.getById(jobId);
        if (job == null) {
            // 不存在则重定向回列表并带错误消息
            model.addAttribute("errorMessage", "职位 ID=" + jobId + " 不存在");
            return "redirect:/job/list";
        }
        model.addAttribute("job", job);
        // 下拉公司列表
        model.addAttribute("companies", companyService.getAllCompanies());
        return "manage/jobEdit";
    }

    /**
     * 处理“编辑职位”提交
     */
    @PostMapping("/edit")
    public String editJob(
            @Valid @ModelAttribute("job") TbJob job,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {
            // 校验失败，回到表单并重载公司列表
            model.addAttribute("companies", companyService.getAllCompanies());
            return "manage/jobEdit";
        }

        boolean updated = jobService.updateJob(job);
        if (updated) {
            redirectAttrs.addFlashAttribute("successMessage", "ID=" + job.getJobId() + " 更新成功");
        } else {
            redirectAttrs.addFlashAttribute("errorMessage", "更新失败");
        }
        return "redirect:/job/list";
    }

    @GetMapping("/application/list")
    public String listJobApplications(Model model) {
        List<TbJobapply> jobapplyList = jobApplicationService.list();
        model.addAttribute("jobapplyList", jobapplyList);
        return "manage/jobApplyList"; // 这里是 jobApplyList.html
    }
}
