package com.flypiggyyoyoyo.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.flypiggyyoyoyo.demo.data.company.register.CompanyRegisterRequest;
import com.flypiggyyoyoyo.demo.data.company.update.CompanyUpdateRequest;
import com.flypiggyyoyoyo.demo.model.TbCompany;
import com.flypiggyyoyoyo.demo.service.TbCompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private TbCompanyService companyService;

    // 添加企业
    @PostMapping("/register")
    public String handleRegister(@Valid CompanyRegisterRequest request,
                                 BindingResult bindingResult,
                                 Model model) {

        log.info("企业图片: {}", request.getCompanyPic());
        companyService.register(request);
        System.out.println("注册完成");
        return "manage/companyList";
    }

    /**
     * 显示企业列表页面
     */
    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String size,
            Model model) {

        // 获取分页数据
        IPage<TbCompany> companyPage = companyService.getCompanyPage(page, pageSize, name, size);

        // 将数据传递给视图
        model.addAttribute("companyList", companyPage.getRecords());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", companyPage.getPages() > 0 ? companyPage.getPages() : 1);
        model.addAttribute("totalCount", companyPage.getTotal());
        model.addAttribute("name", name);
        model.addAttribute("size", size);

        return "manage/companyList"; // 返回Thymeleaf模板路径
    }

    @GetMapping("/edit")
    public String edit(@RequestParam Long companyId, Model model) {
        TbCompany company = companyService.getById(companyId);
        if (company == null) {
            model.addAttribute("errorMsg", "企业不存在");
            return "error/404";
        }
        model.addAttribute("company", company);
        return "manage/companyEdit"; // 返回编辑页面视图名称
    }

    @PostMapping("/update")
    public String updateCompany(
            @RequestParam("companyId") Long companyId,
            @RequestParam("companyName") String companyName,
            @RequestParam("companyArea") String companyArea,
            @RequestParam("companySize") String companySize,
            @RequestParam("companyType") String companyType,
            @RequestParam("companyBrief") String companyBrief,
            @RequestParam("companyState") Integer companyState,
            @RequestParam("companySort") Integer companySort,
            @RequestParam("companyOldPic") String companyPic  // 前端隐藏域
    ) {
        CompanyUpdateRequest req = new CompanyUpdateRequest();
        req.setCompanyId(companyId);
        req.setCompanyName(companyName);
        req.setCompanyArea(companyArea);
        req.setCompanySize(companySize);
        req.setCompanyType(companyType);
        req.setCompanyBrief(companyBrief);
        req.setCompanyState(companyState);
        req.setCompanySort(companySort);
        req.setCompanyPic(companyPic);

        companyService.updateCompany(req);
        return "redirect:/company/list";
    }
}
