package com.ityouzi.system.feign.factory;


import com.ityouzi.core.core.domain.ResultMsg;
import com.ityouzi.system.domain.SysUser;
import com.ityouzi.system.feign.RemoteUserService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService>
{
    @Override
    public RemoteUserService create(Throwable throwable)
    {
        log.error(throwable.getMessage());
        return new RemoteUserService()
        {
            @Override
            public SysUser selectSysUserByUsername(String username) {
                return null;
            }

            @Override
            public ResultMsg updateUserLoginRecord(SysUser user) {
                return ResultMsg.error();
            }
        };
    }
}
