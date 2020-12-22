package com.macro.mall.demo.controller;

import com.macro.mall.common.service.RedisService;
import com.macro.mall.mapper.PmsSkuStockMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @GetMapping("/init")
    public void init() {
        redisService.set(key,1000);
    }

    @PostMapping("/buy")
    public String buy(int quantity,int skuId){
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
            //TODO
            return "购买成功";
        }else {
            //库存不足，超卖，需要把之前减掉的库存加回来
            redisService.incr(key,quantity);
            return "商品库存不足";
        }
    }
}
