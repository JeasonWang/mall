package com.macro.mall.demo.bo;

import org.springframework.amqp.rabbit.connection.CorrelationData;

/**
 * @Author: jeason
 * @Description:
 * @Date: 2021/1/2 14:25
 */
public class MyCorrelationData extends CorrelationData {
    private Object messge;
    private int retryCount;

    public Object getMessge() {
        return messge;
    }

    public void setMessge(Object messge) {
        this.messge = messge;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
