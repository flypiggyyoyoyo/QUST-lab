package com.flypiggyyoyoyo.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrameController {
    // 顶部导航
    @GetMapping("/top.html")
    public String topFrame() {
        return "manage/top"; // 对应 templates/top.html
    }

    // 左侧菜单
    @GetMapping("/left.html")
    public String leftFrame() {
        return "manage/left"; // 对应 templates/left.html
    }

    // 主内容区
    @GetMapping("/index.html")
    public String indexFrame() {
        return "manage/index"; // 对应 templates/index.html
    }

    // 左菜单栏/用户管理
    @GetMapping("/userList.html")
    public String userListFrame() {return "redirect:/user/lists";}

    // 用户添加
    @GetMapping("/user/add")
    public String userAddFrame() {return "manage/userAdd";}

    // 企业管理
    @GetMapping("/companyList.html")
    public String companyListFrame() {return "redirect:/company/list";}

    // 企业添加
    @GetMapping("/company/add")
    public String companyAdd() {
        return "manage/companyAdd";
    }
}
