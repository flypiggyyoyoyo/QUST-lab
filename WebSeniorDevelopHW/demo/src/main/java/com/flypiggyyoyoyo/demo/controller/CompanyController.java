package com.flypiggyyoyoyo.demo.controller;

import com.flypiggyyoyoyo.demo.data.user.register.RegisterRequest;
import com.flypiggyyoyoyo.demo.model.TbUsers;
import com.flypiggyyoyoyo.demo.service.TbCompanyService;
import com.flypiggyyoyoyo.demo.service.TbUsersService;
import com.flypiggyyoyoyo.demo.service.impl.TbCompanyServiceImpl;
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
    public String handleRegister(@Valid RegisterRequest request,
                                 BindingResult bindingResult,
                                 Model model) {

        // System.out.println("注册完成");
        return "manage/main";
    }
}
