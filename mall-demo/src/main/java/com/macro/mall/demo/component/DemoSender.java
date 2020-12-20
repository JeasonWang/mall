package com.macro.mall.demo.component;

import com.macro.mall.common.util.UUID;
import com.macro.mall.demo.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: jeason
 * @Description:
 * @Date: 2020/12/20 15:48
 */
@Component
@Slf4j
public class DemoSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Long orderId){
        //该随机数会到达发送方确认的回调方法，用来判断消息是否已经达到队列
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConfig.DIRECT_EXCHANGE, RabbitConfig.DIRECT_KEY, orderId,correlationData);
        log.info("send orderId:{}",orderId);
    }
}
