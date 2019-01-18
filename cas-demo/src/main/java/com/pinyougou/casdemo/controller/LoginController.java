package com.pinyougou.casdemo.controller;

import com.pinyougou.casdemo.dao.ActionDao;
import com.pinyougou.casdemo.pojo.Member;
import com.pinyougou.casdemo.shiro.KickoutSessionControlFilter;
import com.pinyougou.casdemo.shiro.RetryLimitHashedCredentialsMatcher;
import com.pinyougou.casdemo.shiro.ShiroRealm;
import com.pinyougou.casdemo.shiro.ShiroSessionListener;
import com.pinyougou.casdemo.until.ShiroRedisUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serializable;
import java.util.Deque;

@Controller
public class LoginController {
    @Autowired
    private ActionDao actionDao;
    @Autowired
    private ShiroSessionListener shiroSessionListener;
    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private RetryLimitHashedCredentialsMatcher credentialsMatcher;
    @Autowired
    private ShiroRedisUtils shiroRedisUtils;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //登录
    @PostMapping("/doLogin")
    public String doLogin(String username, String password, boolean rememberMe, String captcha, Model model) {
        Subject subject = SecurityUtils.getSubject();
        //验证验证码
        String code = subject.getSession().getAttribute("KEY_CAPTCHA").toString();
//        if (!captcha.equals(code)) {
//            model.addAttribute("msg", "验证码错误");
//            return "login";
//        }
        //添加用户认证信息
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        try {
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(token);
        } catch (Exception e) {
            model.addAttribute("msg", "用户被锁定");
            return "login";
        }
        return "redirect:/index";
    }

    @RequestMapping(value = "/index")
    public String index(Model model) {
        Subject subject = SecurityUtils.getSubject();
        Member member = (Member) subject.getPrincipal();
        if (member == null) {
            return "login";
        } else {
            model.addAttribute("user", member);
            //统计在线人数
            model.addAttribute("count", shiroSessionListener.getSessionCount());
            return "index";
        }
    }

    //登出
    @RequestMapping(value = "/logout")
    public String logout(Model model) {
        Subject subject = SecurityUtils.getSubject();
        Member member = (Member) subject.getPrincipal();
        //移除redis中的sessionId
        shiroRedisUtils.del(KickoutSessionControlFilter.KICKOUT_KEY + member.getMid());
        subject.logout();
        model.addAttribute("msg", "安全退出！");
        System.out.println("安全退出！");
        return "error";
    }

    //错误页面展示
    @GetMapping("/error")
    public String error(Model model) {
        model.addAttribute("msg", "error页面！");
        System.out.println("error页面！");
        return "error";
    }

    @RequiresPermissions("member:add")
    @RequestMapping(value = "/create")
    public String create(Model model) {
        model.addAttribute("msg", "create页面！");
        System.out.println("create页面！");
        return "create";
    }

    @RequiresPermissions("member:list")
    @RequestMapping(value = "/list")
    public String detail(Model model) {
        model.addAttribute("msg", "list页面！");
        System.out.println("list页面!!");
        return "list";
    }

    /**
     * 新增权限
     *
     * @param rid   用户id
     * @param actid 权限id
     * @return
     */
    @PostMapping("/addAction")
    public String add(String rid, String actid, int c, Model model) {
        if (c == 1) {
            actionDao.addAction(rid, actid);
        } else {
            actionDao.deleteAction(rid, actid);
        }
        //添加成功之后 清除缓存
        Subject subject = SecurityUtils.getSubject();
        Member member = (Member) subject.getPrincipal();
        shiroRedisUtils.del(ShiroRealm.AUTHROLE + member.getMid());
        shiroRedisUtils.del(ShiroRealm.PERMISSIONS + member.getMid());
        return "redirect:/index";
    }

    /**
     * 解锁用户
     */
    @PostMapping("/unlockAccount")
    public String unlockAccount(String username, Model model) {
        model.addAttribute("msg", "用户解锁成功");
        credentialsMatcher.unLockAccount(username);
        System.out.println("用户解锁成功");
        return "redirect:/login";
    }
}