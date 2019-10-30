package com.ityouzi.auth.auth.controller;

import com.ityouzi.auth.auth.model.LoginForm;
import com.ityouzi.auth.auth.service.AccessTokenService;
import com.ityouzi.auth.auth.service.SysLoginService;
import com.ityouzi.core.core.domain.ResultMsg;
import com.ityouzi.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class TokenController {


    @Autowired
    private AccessTokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    /**
     * 用户登录
     */
    @PostMapping("login")
    public ResultMsg login(@RequestBody LoginForm form){
        // 用户登录
        SysUser user = sysLoginService.login(form.getUsername(), form.getPassword());
        // 获取登录token
        Map<String, Object> token = tokenService.createToken(user);
        return ResultMsg.ok(token);
    }

    @PostMapping("logout")
    public ResultMsg logout(HttpServletRequest request){
        String token = request.getHeader("token");
        SysUser user = tokenService.queryByToken(token);        // 依据token 从Redis中获取user
        if (null != user){
            sysLoginService.logout(user.getLoginName());
            tokenService.expireToken(user.getUserId());
        }
        return ResultMsg.ok();
    }

}
