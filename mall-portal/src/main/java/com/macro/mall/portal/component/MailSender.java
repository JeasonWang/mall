package com.macro.mall.portal.component;

import com.alibaba.fastjson.JSON;
import com.macro.mall.model.OmsOrder;
import com.macro.mall.portal.domain.QueueEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: jeason
 * @Description:
 * @Date: 2020/12/22 21:16
 */
@Component
@Slf4j
public class MailSender {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void mailSender(OmsOrder order){
        log.info("send order",order);
        rabbitTemplate.convertAndSend(QueueEnum.QUEUE_ORDER_MAIL.getExchange(),
                QueueEnum.QUEUE_ORDER_MAIL.getRouteKey(),
                JSON.toJSONString(order));
    }
}
