package com.pinyougou.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lenovo on 2018/11/11.
 */
@Controller
public class LoginController {
    @RequestMapping("/")
    public String login() {
        System.out.println("登录接口。。");
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(name);
        return "index";
    }
}
