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
 * @Description:
 * @Date: 2020/12/20 21:05
 */
@RestController
@Slf4j
@RequestMapping("/secondsSkill")
public class SecondsSkillController {
    @Autowired
    private PmsSkuStockMapper skuStockMapper;

    @Autowired
    RedisService redisService;

    private String key = "sku178";


    @GetMapping("/init")
    public void init() {
        redisService.set(key,1000);
    }

    @PostMapping("/buy")
    public String buy(int quantity){
        int l = (int) redisService.get(key);
        if (l<quantity){
            return "库存不足";
        }
        //锁定
        Long result = redisService.decr(key,quantity);
        System.out.println(result);
        return "剩余："+result;
    }
}
