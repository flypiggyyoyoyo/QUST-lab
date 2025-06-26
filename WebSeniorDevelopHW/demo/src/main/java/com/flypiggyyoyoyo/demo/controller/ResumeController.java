package com.flypiggyyoyoyo.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flypiggyyoyoyo.demo.model.TbResumeBasicinfo;
import com.flypiggyyoyoyo.demo.service.TbApplicantService;
import com.flypiggyyoyoyo.demo.service.TbResumeBasicinfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private TbResumeBasicinfoService resumeService;

    @Autowired
    private TbApplicantService applicantService;

    /**
     * 列表页：分页查询简历
     */
    @GetMapping("/lists")
    public String listResumes(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "experience", required = false) String jobExperience,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {
        Page<TbResumeBasicinfo> pageParam = new Page<>(pageNum, pageSize);
        IPage<TbResumeBasicinfo> resumePage = resumeService.pageQuery(pageParam, jobExperience, keyword);
        model.addAttribute("resumePage", resumePage);
        model.addAttribute("currentExperience", jobExperience);
        model.addAttribute("currentKeyword", keyword);
        return "manage/resumeList";
    }

    /**
     * 显示“修改简历”表单
     */
    @GetMapping("/edit")
    public String editForm(@RequestParam Integer id, Model model) {
        TbResumeBasicinfo resume = resumeService.getById(id);
        if (resume == null) {
            log.info("没找到简历，id = {}", id);
            model.addAttribute("errorMsg", "简历不存在");
            return "manage/404";
        }
        model.addAttribute("res", resume);
        model.addAttribute("applicants", applicantService.list()); 
        return "manage/resumeEdit";
    }


    /**
     * 处理“修改简历”提交
     */
    @PostMapping("/edit")
    public String editResume(@ModelAttribute TbResumeBasicinfo res) {
        // 验证 applicantId 是否存在
        if (!applicantService.existsById(res.getApplicantId())) {
            return "redirect:/resume/edit/" + res.getBasicinfoId() + "?error=applicant_not_found";
        }
        resumeService.updateById(res);
        return "redirect:/resume/lists";
    }

    /**
     * 删除简历（单个或批量）
     */
    @PostMapping("/delete")
    public String deleteResumes(@RequestParam("resumeIds") Integer[] ids) {
        resumeService.removeByIds(Arrays.stream(ids).collect(Collectors.toList()));
        return "redirect:/resume/lists";
    }

    /**
     * 显示“添加简历”表单
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("res", new TbResumeBasicinfo());
        model.addAttribute("applicants", applicantService.list()); // 传递申请者列表
        return "manage/resumeAdd";
    }


    /**
     * 处理“添加简历”提交
     */
    @PostMapping("/add")
    public String addResume(@ModelAttribute TbResumeBasicinfo res) {
        // 验证 applicantId 是否存在
        if (!applicantService.existsById(res.getApplicantId())) {
            return "redirect:/resume/add?error=applicant_not_found";
        }
        resumeService.save(res);
        return "redirect:/resume/lists";
    }


}
