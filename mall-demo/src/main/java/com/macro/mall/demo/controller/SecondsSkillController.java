package com.macro.mall.demo.controller;

import com.macro.mall.common.exception.ApiException;
import com.macro.mall.common.service.RedisService;
import com.macro.mall.demo.component.DemoSender;
import com.macro.mall.mapper.PmsSkuStockMapper;
import com.macro.mall.model.PmsSkuStock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.ReentrantLock;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

/**
 * @Author: jeason
 * @Description: https://blog.csdn.net/mycsdnhome/article/details/107459078?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-3.control&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-3.control
 * @Date: 2020/12/20 21:05
 */
@RestController
@Slf4j
@RequestMapping("/secondsSkill")
public class SecondsSkillController {
    @Autowired
    private PmsSkuStockMapper skuStockMapper;

    @Autowired
    private RedisService redisService;

    private String key = "mall-port:product-skuId:stock:"+178;

    @Autowired
    DemoSender demoSender;


    @GetMapping("/init")
    public void init() {
        redisService.set(key,1000);
    }

    @PostMapping("/buyRedis")
    public String buyRedis(Integer quantity,Long skuId){
        Object stock = redisService.get(key);
        // sku缓存不存在，添加
        if (stock == null){
            return "商品不存在redis中";
        }
        int num = (int) stock;
        if (quantity > num){
            return "商品库存不足";
        }
        //减库存，由于和前面的查库存不是原子操作，需要再次检查库存剩余数量
        Long newStock = redisService.decr(key,quantity);
        if (newStock >= 0){
            // 使用异步更新mysql
            demoSender.send(skuId,quantity,10000L);
            return "购买成功";
        }else {
            //库存不足，超卖，需要把之前减掉的库存加回来
            redisService.incr(key,quantity);
            return "商品库存不足";
        }
    }
    ReentrantLock lock = new ReentrantLock();

    @Autowired
    private RetryService retryService;
    @PostMapping("/buyLock")
    public String buyLock(Integer quantity,Long skuId){
        lock.lock();
        try {
            PmsSkuStock skuStock = skuStockMapper.selectByPrimaryKey(skuId);
            int left = skuStock.getStock() - skuStock.getLockStock();
            if (left < quantity){
                return "库存不足";
            }
            int s = skuStockMapper.updateLockStock(skuId,quantity);
            if (s == 1){
                retryService.callback("购买成功",quantity);
                return "购买成功";
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return "购买失败";
    }


}
