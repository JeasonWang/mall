package com.macro.mall.demo.controller;

import com.macro.mall.demo.component.DemoSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: jeason
 * @Description: rabbitmq手动ack练习
 * @Date: 2020/12/20 14:47
 */
@RestController
@RequestMapping("/rabbitmq")
public class RabbitmqController {
    @Autowired
    private DemoSender demoSender;

    @PostMapping("/send")
    public void send(Long message){
        demoSender.send(message);
    }
}
