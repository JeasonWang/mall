package com.macro.mall.common.redis;

import com.macro.mall.common.function.EConsumer;
import lombok.Data;

/**
 * @author jeason
 * @date 2020/4/23 16:26
 */
@Data
public class RedisQueueModel<T> {
    private boolean isStart;
    private Long time=30*60*1000L;
    private Integer size=1;
    private EConsumer<T,Exception> eConsumer;

}
