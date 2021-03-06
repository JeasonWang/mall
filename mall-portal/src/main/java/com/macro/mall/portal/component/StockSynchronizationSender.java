package com.macro.mall.portal.component;

import com.macro.mall.common.util.UUID;
import com.macro.mall.portal.domain.QueueEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
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
public class StockSynchronizationSender {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendStockSynchronizationLock(Long skuId,Integer quantity){
        String sku = skuId+"-"+quantity;
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        log.info("send skuId:[{}],quantity[{}]",skuId,quantity);
        rabbitTemplate.convertAndSend(QueueEnum.QUEUE_STOCK_SYNCHRONIZATION.getExchange(),
                QueueEnum.QUEUE_STOCK_SYNCHRONIZATION.getRouteKey(),
                sku,correlationData);
    }
}
