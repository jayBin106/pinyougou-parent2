package com.pinyougou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.content.service.OrderService;
import com.pinyougou.entity.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lenovo on 2018/11/19.
 */
@RequestMapping("/order")
@Controller
public class OrderController {
    @Reference(version = "1.0.0")
    private OrderService orderService;

    @RequestMapping("/addOrder")
    public Result addOrder() {
//        orderService.add();
        return new Result();
    }

}
