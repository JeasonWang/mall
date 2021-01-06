package com.macro.mall.common.function;

/**
 * @Author: jeason
 * @Description:
 * @Date: 2021/1/6 23:39
 */
@FunctionalInterface
public interface EConsumer<T,E extends Throwable> {
    void accept(T t) throws E;

}
