package com.macro.mall.portal.domain;

import lombok.Getter;

/**
 * 消息队列枚举配置
 *
 * @author jeason
 * @date 2018/9/14
 */
@Getter
public enum QueueEnum {
    /**
     * 消息通知队列
     */
    QUEUE_ORDER_CANCEL("mall.order.direct", "mall.order.cancel", "mall.order.cancel"),
    /**
     * 消息通知ttl队列
     */
    QUEUE_TTL_ORDER_CANCEL("mall.order.direct.ttl", "mall.order.cancel.ttl", "mall.order.cancel.ttl"),

    /**
     * 库存同步队列
     */
    QUEUE_STOCK_SYNCHRONIZATION("mall.stock.direct", "mall.stock.synchronization.queue", "mall.stock.synchronization.key"),

    /**
     * 订单邮件队列
     */
    QUEUE_ORDER_MAIL("mall.order.mail.direct", "mall.order.mail.queue", "mall.order.mail.key");


    /**
     * 交换名称
     */
    private String exchange;
    /**
     * 队列名称
     */
    private String name;
    /**
     * 路由键
     */
    private String routeKey;

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
