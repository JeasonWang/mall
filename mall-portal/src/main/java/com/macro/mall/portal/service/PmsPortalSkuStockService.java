package com.macro.mall.portal.service;

/**
 * @Author: jeason
 * @Description:
 * @Date: 2020/12/22 23:43
 */
public interface PmsPortalSkuStockService {
    /**
     * 数据库和缓存sku库存同步
     */
    void skuStockSynchronization();

    /**
     * sku库存锁定
     * @param skuId
     * @param quantity
     * @return result
     */
    boolean skuStockLock(Long skuId,Integer quantity);
}
