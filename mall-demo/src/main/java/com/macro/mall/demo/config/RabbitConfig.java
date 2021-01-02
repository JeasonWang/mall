package com.macro.mall.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wanghuan
 */
@Configuration
@Slf4j
public class RabbitConfig {
    //普通队列
    public final static String DIRECT_QUEUE="demo_queue";
    public final static String DIRECT_EXCHANGE="DirectExchange";
    public final static String DIRECT_KEY="demo_key";

    //死信队列
    public final static String DIRECT_TTL_QUEUE="demo_ttl_queue";
    public final static String DIRECT_TTL_EXCHANGE="Direct_TTL_Exchange";
    public final static String DIRECT_TTL_KEY="demo_ttl_key";

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
        Jackson2JsonMessageConverter converter=new Jackson2JsonMessageConverter();
        rabbitTemplate.setMessageConverter(converter);
        rabbitTemplate.setEncoding("UTF-8");
        rabbitTemplate.setTaskExecutor(rabbitExecutor());
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

    @Bean(DIRECT_TTL_EXCHANGE)
    public Exchange directTTLExchange(){
        return ExchangeBuilder.directExchange(DIRECT_TTL_EXCHANGE).durable(true).autoDelete().build();
    }
    /**
     * 死信队列过期后发往普通队列
     */
    @Bean(DIRECT_TTL_QUEUE)
    public Queue orderTtlQueue() {
        return QueueBuilder
                .durable(DIRECT_TTL_QUEUE)
                .withArgument("x-dead-letter-exchange", DIRECT_EXCHANGE)//到期后转发的交换机
                .withArgument("x-dead-letter-routing-key", DIRECT_KEY)//到期后转发的路由键
                .build();
    }

    @Bean
    public Binding bindingTTLDirect(@Qualifier(DIRECT_TTL_QUEUE) Queue queue, @Qualifier(DIRECT_TTL_EXCHANGE) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(DIRECT_TTL_KEY).noargs();
    }

}
