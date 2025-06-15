package com.flypiggyyoyoyo.demo.controller;

import com.flypiggyyoyoyo.demo.constants.ErrorEnum;
import com.flypiggyyoyoyo.demo.constants.SuccessEnum;
import com.flypiggyyoyoyo.demo.data.login.LoginRequest;
import com.flypiggyyoyoyo.demo.data.login.LoginResponse;
import com.flypiggyyoyoyo.demo.data.register.RegisterRequest;
import com.flypiggyyoyoyo.demo.model.TbUsers;
import com.flypiggyyoyoyo.demo.service.TbUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UserController {

    // http://127.0.0.1:8080/demo/login
    @RequestMapping("/demo/login")
    public String demoLogin() {
        return "login";
    }

    @Autowired
    private TbUsersService userService;

    // 处理表单提交的登录请求
    @PostMapping("/login")
    public String login(LoginRequest request, Model model, HttpSession session) {
        try {
            // 调用服务层进行登录验证
            LoginResponse response = userService.login(request);

            // TODO: 实现登录验证逻辑
            // 1. 根据用户名查询用户
            // 2. 验证密码是否匹配
            // 3. 验证验证码是否正确
            // 4. 如果验证通过，将用户信息存入session

            // 模拟验证成功
            if (response != null && response.getCode() == SuccessEnum.LOGIN_SUCCESS.getCode()) {
                //session.setAttribute("user", response.getUserName());
                return "redirect:/manage/main"; // 登录成功，重定向到主页
            } else {
                model.addAttribute("errorMsg", "用户名或密码错误");
                return "login"; // 登录失败，返回登录页
            }
        } catch (Exception e) {
            model.addAttribute("errorMsg", "登录异常：" + e.getMessage());
            return "login";
        }
    }

    // 管理主页
    @GetMapping("/manage/main")
    public String mainPage() {
        // System.out.println("进入 mainPage 方法");
        return "manage/main";
    }

    // 添加用户
    @PostMapping("/user/register")
    public String handleRegister(@Valid RegisterRequest request,
                                 BindingResult bindingResult,
                                 Model model) {
        userService.register(request);
        // System.out.println("注册完成");
        return "manage/main";
    }
}
