package com.ityouzi.system.controller;


import com.ityouzi.auth.annotation.HasPermissions;
import com.ityouzi.core.core.controller.BaseController;
import com.ityouzi.core.core.domain.ResultMsg;
import com.ityouzi.log.annotation.OperLog;
import com.ityouzi.log.enums.BusinessType;
import com.ityouzi.system.domain.SysLogininfor;
import com.ityouzi.system.service.ISysLogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 系统访问记录 提供者
 * 用户登录信息控制层
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("logininfor")
public class SysLogininforController extends BaseController
{
    @Autowired
    private ISysLogininforService sysLogininforService;

    /**
     * 查询系统访问记录列表
     */
    @GetMapping("list")
    public ResultMsg list(SysLogininfor sysLogininfor)
    {
        startPage();
        return result(sysLogininforService.selectLogininforList(sysLogininfor));
    }

    /**
     * 新增保存系统访问记录
     */
    @PostMapping("save")
    public void addSave(@RequestBody SysLogininfor sysLogininfor)
    {
        sysLogininforService.insertLogininfor(sysLogininfor);
    }

    
    /**
     * 删除系统访问记录
     */
    @OperLog(title = "访问日志", businessType = BusinessType.DELETE)
    @HasPermissions("monitor:loginlog:remove")
    @PostMapping("remove")
    public ResultMsg remove(String ids)
    {
        return toAjax(sysLogininforService.deleteLogininforByIds(ids));
    }

    @OperLog(title = "访问日志", businessType = BusinessType.CLEAN)
    @HasPermissions("monitor:loginlog:remove")
    @PostMapping("/clean")
    public ResultMsg clean()
    {
        sysLogininforService.cleanLogininfor();
        return ResultMsg.ok();
    }
    
}
