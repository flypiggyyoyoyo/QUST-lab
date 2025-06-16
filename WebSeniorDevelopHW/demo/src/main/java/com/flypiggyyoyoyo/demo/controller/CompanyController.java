package com.flypiggyyoyoyo.demo.controller;


import com.flypiggyyoyoyo.demo.data.company.register.CompanyRegisterRequest;
import com.flypiggyyoyoyo.demo.service.TbCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class CompanyController {
    @Autowired
    private TbCompanyService companyService;

    // 添加企业
    @PostMapping("/company/register")
    public String handleRegister(@Valid CompanyRegisterRequest request,
                                 BindingResult bindingResult,
                                 Model model) {
        companyService.register(request);
        System.out.println("注册完成");
        return "manage/companyList";
    }
}
