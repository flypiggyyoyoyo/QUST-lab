package com.flypiggyyoyoyo.demo.controller;

import com.flypiggyyoyoyo.demo.listener.OnlineUserListener;
import com.flypiggyyoyoyo.demo.model.OnlineUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collection;

@Controller
@RequestMapping("/user")
public class OnlineUserController {

    /** 跳转到在线用户列表页面 **/
    @GetMapping("/online")
    public String onlineList(Model model) {
        Collection<OnlineUser> list = OnlineUserListener.getOnlineUsers();
        System.out.printf("[OnlineUserController] online count = %d%n", list.size());
        model.addAttribute("onlineUsers", list);
        // 对应 Thymeleaf 模板： templates/manage/onlineUserList.html
        return "manage/userOnline";
    }

    /** 强制下线某个用户 **/
    @GetMapping("/offline")
    public String forceOffline(@RequestParam Long userId) {
        for (OnlineUser u : OnlineUserListener.getOnlineUsers()) {
            if (u.getUserId().equals(userId)) {
                HttpSession session = u.getSession();
                try {
                    session.invalidate();
                } catch (IllegalStateException ignore) {
                    // session 已经失效
                }
                break;
            }
        }
        return "redirect:/user/online";
    }
}
