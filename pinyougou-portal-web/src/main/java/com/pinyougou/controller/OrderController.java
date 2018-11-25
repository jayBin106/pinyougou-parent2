package com.pinyougou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.Result;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pojo.TbOrder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by lenovo on 2018/11/19.
 */
@RequestMapping("/order")
@Controller
public class OrderController {
    @Reference(version = "1.0.0", group = "order")
    private OrderService orderService;

    @RequestMapping("/addOrder")
    public Result addOrder(@RequestBody TbOrder order) {
        //获取当前登录人账号
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        order.setUserId(username);
        try {
            order.setSourceType("2");//订单来源  PC
            orderService.add(order);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result("订单生成错误！！");
        }
        return new Result();
    }
}
