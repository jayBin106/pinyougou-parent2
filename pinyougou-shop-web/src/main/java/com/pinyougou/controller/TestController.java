package com.pinyougou.controller;

import com.pinyougou.service.Itest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lenovo on 2018/9/25.
 */
@RestController
public class TestController {
    @Autowired
    Itest abcdfd;

    @RequestMapping("/get")
    public void tets() {
        System.out.println("进入get");
        abcdfd.say();
    }
}
