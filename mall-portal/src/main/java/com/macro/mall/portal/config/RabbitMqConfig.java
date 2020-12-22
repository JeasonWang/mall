package com.macro.mall.portal.config;

import com.macro.mall.portal.domain.QueueEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 消息队列相关配置
 *
 * @author jeason
 * @date 2018/9/14
 */
@Configuration
@Slf4j
public class RabbitMqConfig {

    /**
     * 订单消息实际消费队列所绑定的交换机
     */
    @Bean
    DirectExchange orderDirect() {
        return (DirectExchange) ExchangeBuilder
                .directExchange(QueueEnum.QUEUE_ORDER_CANCEL.getExchange())
                .durable(true)
                .build();
    }

    /**
     * 订单延迟队列队列所绑定的交换机
     */
    @Bean
    DirectExchange orderTtlDirect() {
        return (DirectExchange) ExchangeBuilder
                .directExchange(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange())
                .durable(true)
                .build();
    }

    /**
     * 订单实际消费队列
     */
    @Bean
    public Queue orderQueue() {
        return new Queue(QueueEnum.QUEUE_ORDER_CANCEL.getName());
    }

    /**
     * 订单延迟队列（死信队列）
     */
    @Bean
    public Queue orderTtlQueue() {
        return QueueBuilder
                .durable(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getName())
                .withArgument("x-dead-letter-exchange", QueueEnum.QUEUE_ORDER_CANCEL.getExchange())//到期后转发的交换机
                .withArgument("x-dead-letter-routing-key", QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey())//到期后转发的路由键
                .build();
    }

    /**
     * 将订单队列绑定到交换机
     */
    @Bean
    Binding orderBinding(DirectExchange orderDirect,Queue orderQueue){
        return BindingBuilder
                .bind(orderQueue)
                .to(orderDirect)
                .with(QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey());
    }

    /**
     * 将订单延迟队列绑定到交换机
     */
    @Bean
    Binding orderTtlBinding(DirectExchange orderTtlDirect,Queue orderTtlQueue){
        return BindingBuilder
                .bind(orderTtlQueue)
                .to(orderTtlDirect)
                .with(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey());
    }

    @Bean
    DirectExchange stockDirect(){
        return ExchangeBuilder.directExchange(QueueEnum.QUEUE_STOCK_SYNCHRONIZATION.getExchange()).durable(true).build();
    }

    @Bean
    Queue stockQueue(){
        return new Queue(QueueEnum.QUEUE_STOCK_SYNCHRONIZATION.getName());
    }

    @Bean
    Binding stockBinding(DirectExchange stockDirect,Queue stockQueue){
        return BindingBuilder.bind(stockQueue).to(stockDirect).with(QueueEnum.QUEUE_STOCK_SYNCHRONIZATION.getRouteKey());
    }

//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        /**
//         * ConfirmCallback：每一条发到rabbitmq server的消息都会调一次confirm方法。
//         * 如果消息成功到达exchange，则ack参数为true，反之为false；
//         * cause参数是错误信息；
//         * CorrelationData可以理解为context，在发送消息时传入的这个参数，此时会拿到。
//         * ReturnCallback：成功到达exchange，但routing不到任何queue时会调用。
//         * 可以看到这里能直接拿到message，exchange，routingKey信息。
//         */
//        rabbitTemplate.setConfirmCallback((correlationData,ack,cause) -> {
//            if (ack) {
//                log.info("消息已确认 correlationData:[{}]",correlationData);
//            } else {
//                log.info("消息未确认 cause:[{}]",cause);
//            }
//        });
//        rabbitTemplate.setReturnCallback((message,replyCode,replyText,exchange,routingKey) -> {
//            log.error("消息:{}发送失败,应答码:{},原因:{},交换机:{},路由键:{}",message.toString(),replyCode,replyText,exchange,routingKey);
//        });
//        return rabbitTemplate;
//    }

    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void initRabbitTemplate() {
        /**
         * ConfirmCallback：每一条发到rabbitmq server的消息都会调一次confirm方法。
         * 如果消息成功到达exchange，则ack参数为true，反之为false；
         * cause参数是错误信息；
         * CorrelationData可以理解为context，在发送消息时传入的这个参数，此时会拿到。
         * ReturnCallback：成功到达exchange，但routing不到任何queue时会调用。
         * 可以看到这里能直接拿到message，exchange，routingKey信息。
         */
        rabbitTemplate.setConfirmCallback((correlationData,ack,cause) -> {
            if (ack) {
                log.info("消息已确认 correlationData:[{}]",correlationData);
            } else {
                log.info("消息未确认 cause:[{}]",cause);
            }
        });
        rabbitTemplate.setReturnCallback((message,replyCode,replyText,exchange,routingKey) -> {
            log.error("消息:{}发送失败,应答码:{},原因:{},交换机:{},路由键:{}",message.toString(),replyCode,replyText,exchange,routingKey);
        });
    }

}
