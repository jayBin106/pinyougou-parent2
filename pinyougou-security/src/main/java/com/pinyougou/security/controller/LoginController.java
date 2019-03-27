package com.pinyougou.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@EnableGlobalMethodSecurity(prePostEnabled = true)  //开启角色
public class LoginController {
    @GetMapping("/")
    public String login() {
        return "hello";
    }

    @PreAuthorize("hasRole('ROLE_member')")
    @GetMapping("/adminRole")
    @ResponseBody
    public String adminRole() {
        return "adminRole";
    }

    @PreAuthorize("hasRole('ROLE_user')")
    @GetMapping("/userRole")
    @ResponseBody
    public String userRole() {
        return "userRole";
    }

    //错误页面展示
    @GetMapping("/error")
    public String error(Model model) {
        model.addAttribute("msg", "error页面！");
        System.out.println("error页面！");
        return "error";
    }
}