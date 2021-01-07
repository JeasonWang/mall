package com.macro.mall.factory;

import com.macro.mall.common.util.SpringUtils;
import com.macro.mall.service.UmsAdminService;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author ruoyi
 */
public class AsyncFactory
{

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status 状态
     * @param message 消息
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 插入数据
                SpringUtils.getBean(UmsAdminService.class).insertLoginLog(username);
            }
        };
    }

}
