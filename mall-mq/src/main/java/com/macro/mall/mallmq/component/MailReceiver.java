package com.macro.mall.mallmq.component;

import com.alibaba.fastjson.JSON;
import com.macro.mall.mallmq.service.IMailService;
import com.macro.mall.model.OmsOrder;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @author wanghuan
 */
@Component
public class MailReceiver {

    public static final Logger logger = LoggerFactory.getLogger(MailReceiver.class);

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    IMailService mailService;

    @RabbitListener(queues = "mall.order.mail.queue")
    public void handler(String object, Message message, Channel channel) throws IOException {
        logger.info("receiver message [{}]",object);
        try {
            System.out.println(object);
            OmsOrder order = JSON.parseObject(object, OmsOrder.class);
            //收到消息，发送邮件
            mailService.sendTemplateHtmlMail("2578145258@qq.com","订单邮件",order);
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会再发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            System.out.println("receiver success");
        } catch (IOException e) {
            e.printStackTrace();
            //丢弃这条消息
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            //ack返回false，并重新回到队列
            //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            logger.error("receiver fail:[{}]", object);
        }

    }
}
