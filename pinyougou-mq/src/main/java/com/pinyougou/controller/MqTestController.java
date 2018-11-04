package com.pinyougou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息生产者
 * Created by lenovo on 2018/11/2.
 */
@RestController
@RequestMapping("/mq")
public class MqTestController {
    @Autowired
    JmsMessagingTemplate jmsMessagingTemplate;

    @RequestMapping("/send")
    public void send(String text) {
        jmsMessagingTemplate.convertAndSend("itcast", text);
    }
}
