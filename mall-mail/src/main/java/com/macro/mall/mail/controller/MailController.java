package com.macro.mall.mail.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.mail.domain.OmsOrder;
import com.macro.mall.mail.service.IMailService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: jeason
 * @Description:
 * @Date: 2020/12/29 22:10
 */
@RestController
@RequestMapping("/mail")
public class MailController {
    @Autowired
    IMailService mailService;

    @ApiOperation("订单邮件通知")
    @PostMapping("/orderNoticeMail")
    public CommonResult orderNoticeMail(OmsOrder order){
        mailService.sendTemplateHtmlMail("610813893@qq.com","订单通知",order);
        return CommonResult.success(order.getId());
    }
}
