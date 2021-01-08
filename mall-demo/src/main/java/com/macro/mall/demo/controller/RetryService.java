package com.macro.mall.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author 26950
 * @date 2021/1/8 17:29
 */
@Slf4j
@Service
public class RetryService {
    private static int f = 1;
    @Async
    @Retryable(value = {Exception.class}, maxAttempts = 10, backoff = @Backoff(delay = 1000, multiplier = 2))
    public void callback(String msg,int q) {
        System.out.println(f+msg+q);
        if (f < 15){
            f++;
            int a = 1/0;
        }
        System.out.println(msg+"----------");
    }

    @Recover
    public void recover(Exception e) {
        log.warn("执行还是失败,删掉该任务:{}", e.getMessage());
    }
}
