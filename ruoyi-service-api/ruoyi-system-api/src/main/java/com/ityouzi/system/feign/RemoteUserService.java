package com.ityouzi.system.feign;


import com.ityouzi.core.constant.ServiceNameConstants;
import com.ityouzi.core.core.domain.ResultMsg;
import com.ityouzi.system.domain.SysUser;
import com.ityouzi.system.feign.factory.RemoteUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户 Feign服务层
 * 
 * @author zmr
 * @date 2019-05-20
 */
@FeignClient(name = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService
{
    @GetMapping("user/find/{username}")
    public SysUser selectSysUserByUsername(@PathVariable("username") String username);

    @PostMapping("user/update/login")
    public ResultMsg updateUserLoginRecord(@RequestBody SysUser user);
}
