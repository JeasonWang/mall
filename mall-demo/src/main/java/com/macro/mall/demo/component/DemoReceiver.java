package com.macro.mall.demo.component;

import com.alibaba.fastjson.JSON;
import com.macro.mall.demo.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: jeason
 * @Description:
 * @Date: 2020/12/20 15:49
 */
@Component
@Slf4j
public class DemoReceiver {
    @RabbitListener(queues = RabbitConfig.DIRECT_QUEUE)
    public void receiver(String msg, Channel channel, Message message) throws IOException {
        log.info("ReceiveMessage:"+msg);
        try {
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会再发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            System.out.println("receiver success");
        } catch (IOException e) {
            e.printStackTrace();
            //丢弃这条消息
            //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            //ack返回false，并重新回到队列
            //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            System.out.println("receiver fail");
        }

    }
}
