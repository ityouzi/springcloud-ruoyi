package com.ityouzi.log.publish;

import com.ityouzi.core.constant.Constants;
import com.ityouzi.core.utils.AddressUtils;
import com.ityouzi.core.utils.IpUtils;
import com.ityouzi.core.utils.ServletUtils;
import com.ityouzi.core.utils.spring.SpringContextHolder;
import com.ityouzi.log.event.SysLogininforEvent;
import com.ityouzi.system.domain.SysLogininfor;
import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;

public class PublishFactory {

    /**
     * 记录登录信息
     * @param username      用户名
     * @param status        状态
     * @param message       消息
     * @param args          列表
     */
    public static void recordLogininfor(final String username,
                                       final String status,
                                       final String message,
                                       final Object ... args){


        HttpServletRequest request = ServletUtils.getRequest();
        // 获取用户登录设备
        final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 获取用户IP
        final String ip = IpUtils.getIpAddr(request);
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();

        // 封装对象
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setLoginName(username);
        logininfor.setIpaddr(ip);
        logininfor.setLoginLocation(AddressUtils.getRealAddressByIP(ip));   // 依据IP获取网络信息
        logininfor.setBrowser(browser);
        logininfor.setOs(os);
        logininfor.setMsg(message);

        // 日志状态
        if (Constants.LOGIN_SUCCESS.equals(status) || Constants.LOGOUT.equals(status)){
            logininfor.setStatus(Constants.SUCCESS);
        }
        else if (Constants.LOGIN_FAIL.equals(status)){
            logininfor.setStatus(Constants.FAIL);
        }
        // 发布事件
        SpringContextHolder.publishEvent(new SysLogininforEvent(logininfor));
    }


}
