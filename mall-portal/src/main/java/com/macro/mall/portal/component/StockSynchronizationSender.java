package com.macro.mall.portal.component;

import com.macro.mall.portal.domain.QueueEnum;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: jeason
 * @Description:
 * @Date: 2020/12/22 21:16
 */
@Component
public class StockSynchronizationSender {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendStockSynchronization(Long skuId,Integer quantity){
        String s = skuId+"-"+quantity;
        rabbitTemplate.convertAndSend(QueueEnum.QUEUE_STOCK_SYNCHRONIZATION.getExchange(),
                QueueEnum.QUEUE_STOCK_SYNCHRONIZATION.getRouteKey(),
                s);
    }
}
