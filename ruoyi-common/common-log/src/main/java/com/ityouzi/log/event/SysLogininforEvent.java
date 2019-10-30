package com.ityouzi.log.event;

import org.springframework.context.ApplicationEvent;

/**
 * 系统登录日志事件
 */
public class SysLogininforEvent extends ApplicationEvent {

    private static final long serialVersionUID = -9084676463718966036L;


    public SysLogininforEvent(Object source) {
        super(source);
    }
}
