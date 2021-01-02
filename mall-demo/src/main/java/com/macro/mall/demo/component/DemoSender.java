package com.macro.mall.demo.component;

import com.alibaba.fastjson.JSON;
import com.macro.mall.common.util.UUID;
import com.macro.mall.demo.config.RabbitConfig;
import com.macro.mall.demo.dto.SkuParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
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
    private RabbitTemplate rabbitTemplate;

    public void send(Long skuId,Integer quantity,Long delayTimes){
        //该随机数会到达发送方确认的回调方法，用来判断消息是否已经达到队列
        SkuParam skuParam = new SkuParam(skuId,quantity);
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConfig.DIRECT_TTL_EXCHANGE,
                RabbitConfig.DIRECT_TTL_KEY,
                JSON.toJSON(skuParam),
                new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        //给消息设置延迟毫秒值
                        message.getMessageProperties().setExpiration(String.valueOf(delayTimes));
                        return message;
                    }
                },
                correlationData);
        log.info("send orderId:{}",skuParam);
    }
}
