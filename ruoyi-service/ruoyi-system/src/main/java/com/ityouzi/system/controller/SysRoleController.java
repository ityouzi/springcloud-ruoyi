package com.ityouzi.system.controller;


import com.ityouzi.auth.annotation.HasPermissions;
import com.ityouzi.core.core.controller.BaseController;
import com.ityouzi.core.core.domain.ResultMsg;
import com.ityouzi.log.annotation.OperLog;
import com.ityouzi.log.enums.BusinessType;
import com.ityouzi.system.domain.SysRole;
import com.ityouzi.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 角色 提供者
 * 
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("role")
public class SysRoleController extends BaseController
{
    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * 查询角色
     */
    @GetMapping("get/{roleId}")
    public SysRole get(@PathVariable("roleId") Long roleId)
    {
        return sysRoleService.selectRoleById(roleId);
    }

    /**
     * 查询角色列表
     */
    @GetMapping("list")
    public ResultMsg list(SysRole sysRole)
    {
        startPage();
        return result(sysRoleService.selectRoleList(sysRole));
    }

    @GetMapping("all")
    public ResultMsg all()
    {
        return ResultMsg.ok().put("rows", sysRoleService.selectRoleAll());
    }

    /**
     * 新增保存角色
     */
    @PostMapping("save")
    @OperLog(title = "角色管理", businessType = BusinessType.INSERT)
    public ResultMsg addSave(@RequestBody SysRole sysRole)
    {
        return toAjax(sysRoleService.insertRole(sysRole));
    }

    /**
     * 修改保存角色
     */
    @OperLog(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("update")
    public ResultMsg editSave(@RequestBody SysRole sysRole)
    {
        return toAjax(sysRoleService.updateRole(sysRole));
    }

    /**
     * 修改保存角色
     */
    @OperLog(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("status")
    public ResultMsg status(@RequestBody SysRole sysRole)
    {
        return toAjax(sysRoleService.changeStatus(sysRole));
    }
    
    /**
     * 保存角色分配数据权限
     */
    @HasPermissions("system:role:edit")
    @OperLog(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/authDataScope")
    public ResultMsg authDataScopeSave(@RequestBody SysRole role)
    {
        role.setUpdateBy(getLoginName());
        if (sysRoleService.authDataScope(role) > 0)
        {
            return ResultMsg.ok();
        }
        return ResultMsg.error();
    }

    /**
     * 删除角色
     * @throws Exception 
     */
    @OperLog(title = "角色管理", businessType = BusinessType.DELETE)
    @PostMapping("remove")
    public ResultMsg remove(String ids) throws Exception
    {
        return toAjax(sysRoleService.deleteRoleByIds(ids));
    }
}
