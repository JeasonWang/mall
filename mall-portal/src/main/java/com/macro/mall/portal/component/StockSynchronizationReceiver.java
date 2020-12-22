package com.macro.mall.portal.component;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: jeason
 * @Description:
 * @Date: 2020/12/22 21:17
 */
@Slf4j
@Component
public class StockSynchronizationReceiver {

    @RabbitListener(queues = "mall.stock.synchronization.queue")
    public void receiver(String s, Channel channel, Message message) throws IOException {
        log.info("Receive orderId:[{}]",s);
        try{
            System.out.println(s);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);
            log.info("订单取消消息消费异常[{}]",s);
        }
    }
}
