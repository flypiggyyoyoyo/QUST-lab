package com.flypiggyyoyoyo.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flypiggyyoyoyo.demo.model.TbApplicant;
import com.flypiggyyoyoyo.demo.model.TbResumeBasicinfo;
import com.flypiggyyoyoyo.demo.service.TbApplicantService;
import com.flypiggyyoyoyo.demo.service.TbResumeBasicinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class ApplicantController {

    @Autowired
    private TbApplicantService applicantService;

    @Autowired
    private TbResumeBasicinfoService resumeBasicInfoService; // 注入简历服

    /**
     * 显示添加申请者页面
     * @return 返回视图名称 "applicant/add"
     */
    @GetMapping("/applicant/add")
    public String showAddForm() {
        return "manage/applicantAdd";
    }

    /**
     * 处理添加申请者表单提交
     * @param applicantEmail 申请者邮箱
     * @param applicantPwd 申请者密码
     * @return 重定向到申请者列表页面
     */
    @PostMapping("/applicant/add")
    public String addApplicant(
            @RequestParam("applicantEmail") String applicantEmail,
            @RequestParam("applicantPwd") String applicantPwd,
            @RequestParam("applicantName") String applicantName) {
        TbApplicant applicant = new TbApplicant();
        applicant.setApplicantEmail(applicantEmail);
        applicant.setApplicantPwd(applicantPwd);
        applicant.setApplicantName(applicantName);
        applicant.setApplicantRegistdate(new Date());
        applicantService.save(applicant);
        return "redirect:/applicant/list";
    }

    /**
     * 显示编辑求职者页面
     * @param applicantId 求职者ID，从URL参数获取
     * @param model 用于传递数据到视图
     * @return 返回视图名称 "applicant/edit"
     */
    @GetMapping("/applicant/edit")
    public String showEditForm(@RequestParam("applicantId") Long applicantId, Model model) {
        // 根据ID查询求职者信息
        TbApplicant applicant = applicantService.getById(applicantId);
        // 将求职者对象添加到模型中，供页面使用
        model.addAttribute("applicant", applicant);
        return "manage/applicantEdit"; // 渲染 templates/applicant/edit.html
    }

    /**
     * 处理编辑求职者表单提交
     * @param applicantId 求职者ID
     * @param applicantEmail 新的邮箱
     * @param applicantPwd 新的密码
     * @return 重定向到求职者列表页面
     */
    @PostMapping("/applicant/update")
    public String updateApplicant(
            @RequestParam("applicantId") int applicantId,
            @RequestParam("applicantEmail") String applicantEmail,
            @RequestParam("applicantPwd") String applicantPwd,
            @RequestParam("applicantName") String applicantName) {
        // 创建求职者对象并设置更新后的值
        TbApplicant applicant = new TbApplicant();
        applicant.setApplicantId(applicantId);
        applicant.setApplicantEmail(applicantEmail);
        applicant.setApplicantPwd(applicantPwd);
        applicant.setApplicantName(applicantName);
        applicantService.updateById(applicant);

        return "redirect:/applicant/list";
    }

    @GetMapping("/applicant/list")
    public String listApplicants(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {
        Page<TbApplicant> page = new Page<>(pageNum, pageSize);
        QueryWrapper<TbApplicant> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.like("applicant_email", keyword);
        }
        IPage<TbApplicant> applicantPage = applicantService.page(page, queryWrapper);
        model.addAttribute("applicants", applicantPage.getRecords());
        model.addAttribute("applicantPage", applicantPage);
        model.addAttribute("currentKeyword", keyword);
        return "manage/applicantList";
    }

    /**
     * 删除求职者及其相关简历信息
     * @param applicantId 求职者ID，从URL参数获取
     * @return 重定向到求职者列表页面
     */
    @GetMapping("/applicant/delete")
    @Transactional // 确保操作的原子性
    public String deleteApplicant(@RequestParam("applicantId") Long applicantId) {
        // 第一步：删除 tb_resume_basicinfo 表中相关的记录
        QueryWrapper<TbResumeBasicinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("APPLICANT_ID", applicantId);
        resumeBasicInfoService.remove(queryWrapper);

        // 第二步：删除 tb_applicant 表中的记录
        applicantService.removeById(applicantId);

        // 重定向到列表页面
        return "redirect:/applicant/list";
    }
}
