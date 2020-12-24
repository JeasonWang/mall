package com.macro.mall.portal.component;

import com.macro.mall.portal.service.PmsPortalSkuStockService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    PmsPortalSkuStockService portalSkuStockService;

    @RabbitListener(queues = "mall.stock.synchronization.queue")
    public void receiver(String sku, Channel channel, Message message) throws IOException {
        log.info("Receive orderId:[{}]",sku);
        try{
            System.out.println(sku);
            String[] ss = sku.split("-");
            Long skuId = Long.valueOf(ss[0]);
            Integer quantity = Integer.valueOf(ss[1]);
            boolean result = portalSkuStockService.skuStockLock(skuId,quantity);
            if (!result){
                log.error("库存锁定异常[{}]",sku);
            }else {
                log.info("库存锁定成功[{}]",sku);
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);
            log.info("库存锁定异常[{}]",sku);
        }
    }
}
