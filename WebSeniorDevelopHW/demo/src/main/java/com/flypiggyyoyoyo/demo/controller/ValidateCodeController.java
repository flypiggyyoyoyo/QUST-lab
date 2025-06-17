package com.flypiggyyoyoyo.demo.controller;

import com.flypiggyyoyoyo.demo.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/api")
public class ValidateCodeController {

    // 验证码图片生成接口
    @GetMapping("/captcha")
    public void generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置响应头
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 获取Session
        HttpSession session = request.getSession();

        // 生成验证码图片并获取验证码文本
        String code = ValidateCodeUtils.generateCodeImage(
                100, 40, 4, response.getOutputStream()
        );

        log.info("验证码重刷");

        // 将验证码存储到Session中
        session.setAttribute("validateCode", code);
    }

    // 验证验证码接口（用于AJAX验证）
    @PostMapping("/verifyCaptcha")
    @ResponseBody
    public Map<String, Object> verifyCaptcha(
            @RequestParam("userInput") String userInput,
            HttpServletRequest request) {

        Map<String, Object> result = new HashMap<>();

        // 从Session获取存储的验证码
        HttpSession session = request.getSession();
        String storedCode = (String) session.getAttribute("validateCode");

        // 验证验证码
        if (storedCode == null || !storedCode.equalsIgnoreCase(userInput)) {
            result.put("success", false);
            result.put("message", "验证码错误");
        } else {
            result.put("success", true);
            result.put("message", "验证通过");

            // 验证通过后，移除Session中的验证码，防止重复使用
            session.removeAttribute("validateCode");
        }

        return result;
    }
}