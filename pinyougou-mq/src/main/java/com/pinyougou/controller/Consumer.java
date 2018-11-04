package com.pinyougou.controller;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费者
 * Created by lenovo on 2018/11/2.
 */
@Component
public class Consumer {
    @JmsListener(destination = "itcast")
    public void readMessage(String text) {
        System.out.println("接收到信息：" + text);
        System.out.println("接收到信息：" + text);
    }
}
