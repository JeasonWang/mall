package com.macro.mall.factory;

import com.macro.mall.common.ip.AddressUtils;
import com.macro.mall.common.ip.IpUtils;
import com.macro.mall.common.util.ServletUtils;
import com.macro.mall.common.util.SpringUtils;
import com.macro.mall.mapper.UmsAdminLoginLogMapper;
import com.macro.mall.model.UmsAdmin;
import com.macro.mall.model.UmsAdminLoginLog;
import com.macro.mall.service.UmsAdminService;
import eu.bitwalker.useragentutils.UserAgent;

import java.util.Date;
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
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));

        return new TimerTask()
        {
            @Override
            public void run()
            {
                UmsAdmin admin = SpringUtils.getBean(UmsAdminService.class).getAdminByUsername(username);
                if(admin==null) {
                    return;
                }
                UmsAdminLoginLog loginLog = new UmsAdminLoginLog();
                loginLog.setAdminId(admin.getId());
                loginLog.setCreateTime(new Date());
                loginLog.setIp(ip);
                loginLog.setAddress(AddressUtils.getRealAddressByIP(ip));
                loginLog.setUserAgent(userAgent.getBrowser().getName());
                SpringUtils.getBean(UmsAdminLoginLogMapper.class).insert(loginLog);
            }
        };
    }

}
