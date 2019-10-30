package com.ityouzi.auth.auth.service;


import com.ityouzi.core.constant.Constants;
import com.ityouzi.core.constant.UserConstants;
import com.ityouzi.core.enums.UserStatus;
import com.ityouzi.core.exception.user.UserBlockedException;
import com.ityouzi.core.exception.user.UserDeleteException;
import com.ityouzi.core.exception.user.UserNotExistsException;
import com.ityouzi.core.exception.user.UserPasswordNotMatchException;
import com.ityouzi.core.utils.*;

import com.ityouzi.log.publish.PublishFactory;

import com.ityouzi.system.domain.SysUser;
import com.ityouzi.system.feign.RemoteUserService;
import com.ityouzi.system.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SysLoginService {


    @Autowired
    private RemoteUserService userService;


    /**
     * 登录
     */
    public SysUser login(String username, String password)
    {
        // 验证码校验
//         if
//         (!StringUtils.isEmpty(ServletUtils.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA)))
//         {
//         AsyncManager.me().execute(AsyncFactory.recordLogininfor(username,
//         Constants.LOGIN_FAIL,
//         MessageUtils.message("user.jcaptcha.error")));
//         throw new CaptchaException();
//         }
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password))
        {
            PublishFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null"));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            PublishFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.not.match"));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            PublishFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.not.match"));
            throw new UserPasswordNotMatchException();
        }
        // 查询用户信息
        SysUser user = userService.selectSysUserByUsername(username);
        // if (user == null && maybeMobilePhoneNumber(username))
        // {
        // user = userService.selectUserByPhoneNumber(username);
        // }
        // if (user == null && maybeEmail(username))
        // {
        // user = userService.selectUserByEmail(username);
        // }
        if (user == null) {
            PublishFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.not.exists"));
            throw new UserNotExistsException();
        }
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            PublishFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.delete"));
            throw new UserDeleteException();
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            PublishFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.blocked", user.getRemark()));
            throw new UserBlockedException();
        }
        if (!PasswordUtil.matches(user, password))
        {
            throw new UserPasswordNotMatchException();
        }
        PublishFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        recordLoginInfo(user);
        return user;
    }

    // private boolean maybeEmail(String username)
    // {
    // if (!username.matches(UserConstants.EMAIL_PATTERN))
    // {
    // return false;
    // }
    // return true;
    // }
    //
    // private boolean maybeMobilePhoneNumber(String username)
    // {
    // if (!username.matches(UserConstants.MOBILE_PHONE_NUMBER_PATTERN))
    // {
    // return false;
    // }
    // return true;
    // }
    /**
     * 记录登录信息
     */
    public void recordLoginInfo(SysUser user)
    {
        user.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        user.setLoginDate(DateUtils.getNowDate());
        userService.updateUserLoginRecord(user);
    }

    public void logout(String loginName)
    {
        PublishFactory.recordLogininfor(loginName, Constants.LOGOUT,
                MessageUtils.message("user.logout.success"));
    }
}