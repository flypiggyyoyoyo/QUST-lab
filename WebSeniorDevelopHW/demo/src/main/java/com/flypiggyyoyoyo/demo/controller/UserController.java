package com.flypiggyyoyoyo.demo.controller;

import com.flypiggyyoyoyo.demo.data.user.login.UserLoginRequest;
import com.flypiggyyoyoyo.demo.data.user.login.UserLoginResponse;
import com.flypiggyyoyoyo.demo.data.user.register.UserRegisterRequest;
import com.flypiggyyoyoyo.demo.exception.UserException;
import com.flypiggyyoyoyo.demo.service.TbUsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
public class UserController {

    // http://127.0.0.1:8080/demo/login
    @RequestMapping("/demo/login")
    public String demoLogin() {
        log.info("登录页面");
        return "login";
    }

    @Autowired
    private TbUsersService userService;

    @PostMapping("/login")
    public String login(UserLoginRequest request, Model model, HttpSession session, HttpServletRequest servletRequest) {
        try {
            // 调用服务层进行登录验证（传递HttpServletRequest参数）
            UserLoginResponse response = userService.login(request, servletRequest);

            log.info("验证成功，用户: {}", response.getUserName());

            // 验证通过，将用户信息存入session
            session.setAttribute("loginUser", response.getUserId()); // 存储用户ID
            session.setAttribute("userName", response.getUserName()); // 存储用户名
            session.setAttribute("userRole", response.getRole()); // 存储用户角色

            // 设置Session超时时间（30分钟）
            session.setMaxInactiveInterval(30 * 60);

            return "redirect:/manage/main"; // 登录成功，重定向到主页
        } catch (UserException e) {
            // 业务异常，记录错误信息但不记录堆栈
            log.warn("登录失败：{}", e.getMessage());
            model.addAttribute("errorMsg", e.getMessage());
            return "login";
        } catch (Exception e) {
            // 系统异常，记录完整堆栈信息
            log.error("登录系统异常：", e);
            model.addAttribute("errorMsg", "登录异常，请稍后重试");
            // 可以添加额外的错误处理逻辑，如告警等
            return "login";
        }
    }

    // 退出接口：重定向到/demo/login
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        log.info("执行到这里！！！");
        // return "redirect:/demo/login"; // 重定向到demoLogin接口
        return "redirect:/demo/login";
    }

    // 管理主页
    @GetMapping("/manage/main")
    public String mainPage() {
        System.out.println("进入 mainPage 方法");
        return "manage/main";
    }

    // 添加用户
    @PostMapping("/user/register")
    public String handleRegister(@Valid UserRegisterRequest request,
                                 BindingResult bindingResult,
                                 Model model) {
        userService.register(request);
        // System.out.println("注册完成");
        return "manage/userList";
    }
}
