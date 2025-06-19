package com.flypiggyyoyoyo.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flypiggyyoyoyo.demo.data.company.update.CompanyUpdateRequest;
import com.flypiggyyoyoyo.demo.data.user.login.UserLoginRequest;
import com.flypiggyyoyoyo.demo.data.user.login.UserLoginResponse;
import com.flypiggyyoyoyo.demo.data.user.register.UserRegisterRequest;
import com.flypiggyyoyoyo.demo.data.user.update.UserUpdateRequest;
import com.flypiggyyoyoyo.demo.exception.UserException;
import com.flypiggyyoyoyo.demo.model.TbCompany;
import com.flypiggyyoyoyo.demo.model.TbUsers;
import com.flypiggyyoyoyo.demo.service.TbUsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @ResponseBody // 关键：添加此注解返回JSON而非视图
    public Map<String, Object> login(UserLoginRequest request,
                                     HttpSession session,
                                     HttpServletRequest servletRequest) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 调用服务层进行登录验证
            UserLoginResponse response = userService.login(request, servletRequest);

            log.info("验证成功，用户: {}", response.getUserName());

            // 验证通过，将用户信息存入session
            session.setAttribute("loginUser", response.getUserId());
            session.setAttribute("userName", response.getUserName());
            session.setAttribute("userRole", response.getRole());

            // 设置Session超时时间
            session.setMaxInactiveInterval(30 * 60);

            // 登录成功
            result.put("code", 200);
            result.put("msg", "登录成功");
            result.put("data", response);
        } catch (UserException e) {
            // 业务异常
            log.warn("登录失败：{}", e.getMessage());
            result.put("code", 400);
            result.put("msg", e.getMessage());
        } catch (Exception e) {
            // 系统异常
            log.error("登录系统异常：", e);
            result.put("code", 500);
            result.put("msg", "登录异常，请稍后重试");
        }

        return result;
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
        //return "manage/userList";
        return "redirect:/user/lists";
    }

    @GetMapping("/user/lists")
    public String listUsers(
            Model model,
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String keyword) {

        Page<TbUsers> page = new Page<>(pageNum, pageSize);
        Page<TbUsers> userPage = userService.findByRoleAndKeyword(page, role, keyword);

        // ① 把分页对象放进去（你已经有的）
        model.addAttribute("userPage", userPage);
        // ② 再把真正的列表放进去，供 th:each 使用
        model.addAttribute("users", userPage.getRecords());

        model.addAttribute("currentRole", role);
        model.addAttribute("currentKeyword", keyword);
        return "manage/userList";
    }

    @GetMapping("/user/edit")
    public String edit(@RequestParam Long userId, Model model) {
        TbUsers user = userService.getById(userId);
        if (user == null) {
            model.addAttribute("errorMsg", "用户不存在");
            return "error/404";
        }
        model.addAttribute("user", user);
        return "manage/userEdit"; // 返回编辑页面视图名称
    }

    @PostMapping("/user/update")
    public String updateUser(@ModelAttribute UserUpdateRequest req) {
        userService.updateUser(req);
        return "redirect:/user/lists";
    }
}
