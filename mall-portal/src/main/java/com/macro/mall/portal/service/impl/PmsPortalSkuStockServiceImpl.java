package com.macro.mall.portal.service.impl;

import com.macro.mall.common.service.RedisService;
import com.macro.mall.mapper.PmsSkuStockMapper;
import com.macro.mall.model.PmsSkuStock;
import com.macro.mall.portal.service.PmsPortalSkuStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: jeason
 * @Description:
 * @Date: 2020/12/22 23:48
 */
@Service
public class PmsPortalSkuStockServiceImpl implements PmsPortalSkuStockService {
    @Autowired
    RedisService redisService;
    @Autowired
    PmsSkuStockMapper pmsSkuStockMapper;

    private String prixKey = "mall-port:product-skuId:stock:";

    @Override
    public void skuStockSynchronization() {
        List<PmsSkuStock> pmsSkuStocks = pmsSkuStockMapper.selectAllAvailableSku();
        for(PmsSkuStock sku : pmsSkuStocks){
            redisService.set(prixKey + sku.getId(),sku.getStock() - sku.getLockStock());
        }
    }

    @Override
    public boolean skuStockLock(Long skuId, Integer quantity) {
        return pmsSkuStockMapper.updateLockStock(skuId,quantity)>0;
    }
}
