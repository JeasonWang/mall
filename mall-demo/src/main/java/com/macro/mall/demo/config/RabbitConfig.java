package com.macro.mall.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wanghuan
 */
@Configuration
@Slf4j
public class RabbitConfig {
    public final static String DIRECT_QUEUE="mall.stock.synchronization.queue";
    public final static String DIRECT_EXCHANGE="mall.stock.direct";
    public final static String DIRECT_KEY="mall.stock.synchronization.key";
    @Resource
    private RabbitTemplate template;

    @Value("${spring.rabbitmq.listener.simple.max-concurrency}")
    private int rabbitMaxSize;

    @Bean("rabbitExecutor")
    public Executor rabbitExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(rabbitMaxSize);
        executor.setMaxPoolSize(rabbitMaxSize);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("rabbitThread-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    @Bean
    public AmqpTemplate amqpTemplate(SimpleRabbitListenerContainerFactory factory){
        Jackson2JsonMessageConverter converter=new Jackson2JsonMessageConverter();
        factory.setTaskExecutor(rabbitExecutor());
        template.setMessageConverter(converter);
        template.setEncoding("UTF-8");
        //消息发送到RabbitMQ交换器,但无相应队列与交换器绑定时的回调
        template.setReturnCallback((message,replyCode,replyText,exchange,routingKey)->{
            String correlation=message.getMessageProperties().getCorrelationId();
            log.error("消息:{}发送失败,应答码:{},原因:{},交换机:{},路由键:{}",message.toString(),replyCode,replyText,exchange,routingKey);
        });
        //消息发送到RabbitMQ交换器后接收ack回调,如果消息发送确认失败就进行重试.
        template.setConfirmCallback(this::confirm);
        return template;
    }

    private void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("消息已确认 correlationData:[{}]",correlationData);
        } else {
            log.info("消息未确认 cause:[{}]",cause);
        }
    }

    @Bean(DIRECT_EXCHANGE)
    public Exchange directExchange(){
        return ExchangeBuilder.directExchange(DIRECT_EXCHANGE).durable(true).autoDelete().build();
    }
    @Bean(DIRECT_QUEUE)
    public Queue directQueue(){
        return QueueBuilder.durable(DIRECT_QUEUE).build();
    }

    @Bean
    public Binding bindingDirect(@Qualifier(DIRECT_QUEUE) Queue queue, @Qualifier(DIRECT_EXCHANGE) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(DIRECT_KEY).noargs();
    }

}
