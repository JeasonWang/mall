package com.macro.mall.common.api;

/**
 * 封装API的错误码
 * Created by macro on 2019/4/19.
 */
public interface IErrorCode {
    /**
     * 状态码
     * @return
     */
    long getCode();

    /**
     * 消息
     * @return
     */
    String getMessage();
}
