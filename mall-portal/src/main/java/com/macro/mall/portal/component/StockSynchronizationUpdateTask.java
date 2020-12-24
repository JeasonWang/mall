package com.macro.mall.portal.component;

import com.macro.mall.portal.service.PmsPortalSkuStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author jeason
 * @date 2018/8/24
 * mysql和redis库存同步更新
 */
@Component
public class StockSynchronizationUpdateTask {
    private Logger logger =LoggerFactory.getLogger(StockSynchronizationUpdateTask.class);
    @Autowired
    private PmsPortalSkuStockService portalSkuStockService;

    /**
     * cron表达式：Seconds Minutes Hours DayofMonth Month DayofWeek [Year]
     * 每天凌晨三点同步mysql库存至redis
     */
    @Scheduled(cron = "0 0/15 * ? * ?")
    private void mysqlAndRedisStockSynchronization(){
        portalSkuStockService.skuStockSynchronization();
        logger.info("每天凌晨三点同步mysql库存至redis");
    }
}
