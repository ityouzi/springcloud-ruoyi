package com.ityouzi.system.controller;


import com.ityouzi.auth.annotation.HasPermissions;
import com.ityouzi.core.annotation.LoginUser;
import com.ityouzi.core.constant.UserConstants;
import com.ityouzi.core.core.controller.BaseController;
import com.ityouzi.core.core.domain.ResultMsg;
import com.ityouzi.core.utils.RandomUtil;
import com.ityouzi.log.annotation.OperLog;
import com.ityouzi.log.enums.BusinessType;
import com.ityouzi.system.domain.SysUser;
import com.ityouzi.system.service.ISysMenuService;
import com.ityouzi.system.service.ISysUserService;
import com.ityouzi.system.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户 提供者
 * 
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("user")
public class SysUserController extends BaseController
{
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysMenuService sysMenuService;

    /**
     * 查询用户
     */
    @GetMapping("get/{userId}")
    public SysUser get(@PathVariable("userId") Long userId)
    {
        return sysUserService.selectUserById(userId);
    }

    @GetMapping("info")
    public SysUser info(@LoginUser SysUser sysUser)
    {
        sysUser.setButtons(sysMenuService.selectPermsByUserId(sysUser.getUserId()));
        return sysUser;
    }

    /**
     * 查询用户
     */
    @GetMapping("find/{username}")
    public SysUser findByUsername(@PathVariable("username") String username)
    {
        return sysUserService.selectUserByLoginName(username);
    }

    /**
     * 查询用户列表
     */
    @GetMapping("list")
    public ResultMsg list(SysUser sysUser)
    {
        startPage();
        return result(sysUserService.selectUserList(sysUser));
    }

    /**
     * 新增保存用户
     */
    @HasPermissions("system:user:add")
    @PostMapping("save")
    @OperLog(title = "用户管理", businessType = BusinessType.INSERT)
    public ResultMsg addSave(@RequestBody SysUser sysUser)
    {
        if (UserConstants.USER_NAME_NOT_UNIQUE.equals(sysUserService.checkLoginNameUnique(sysUser.getLoginName())))
        {
            return ResultMsg.error("新增用户'" + sysUser.getLoginName() + "'失败，登录账号已存在");
        }
        else if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(sysUserService.checkPhoneUnique(sysUser)))
        {
            return ResultMsg.error("新增用户'" + sysUser.getLoginName() + "'失败，手机号码已存在");
        }
        else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(sysUserService.checkEmailUnique(sysUser)))
        {
            return ResultMsg.error("新增用户'" + sysUser.getLoginName() + "'失败，邮箱账号已存在");
        }
        sysUser.setSalt(RandomUtil.randomStr(6));
        sysUser.setPassword(
                PasswordUtil.encryptPassword(sysUser.getLoginName(), sysUser.getPassword(), sysUser.getSalt()));
        sysUser.setCreateBy(getLoginName());
        return toAjax(sysUserService.insertUser(sysUser));
    }

    /**
     * 修改保存用户
     */
    @HasPermissions("system:user:edit")
    @OperLog(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("update")
    public ResultMsg editSave(@RequestBody SysUser sysUser)
    {
        if (null != sysUser.getUserId() && SysUser.isAdmin(sysUser.getUserId()))
        {
            return ResultMsg.error("不允许修改超级管理员用户");
        }
        else if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(sysUserService.checkPhoneUnique(sysUser)))
        {
            return ResultMsg.error("修改用户'" + sysUser.getLoginName() + "'失败，手机号码已存在");
        }
        else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(sysUserService.checkEmailUnique(sysUser)))
        {
            return ResultMsg.error("修改用户'" + sysUser.getLoginName() + "'失败，邮箱账号已存在");
        }
        return toAjax(sysUserService.updateUser(sysUser));
    }

    /**
     * 修改用户信息
     * @param sysUser
     * @return
     * @author zmr
     */
    @HasPermissions("system:user:edit")
    @PostMapping("update/info")
    @OperLog(title = "用户管理", businessType = BusinessType.UPDATE)
    public ResultMsg updateInfo(@RequestBody SysUser sysUser)
    {
        return toAjax(sysUserService.updateUserInfo(sysUser));
    }

    /**
     * 记录登陆信息
     * @param sysUser
     * @return
     * @author zmr
     */
    @PostMapping("update/login")
    public ResultMsg updateLoginRecord(@RequestBody SysUser sysUser)
    {
        return toAjax(sysUserService.updateUser(sysUser));
    }

    @HasPermissions("system:user:resetPwd")
    @OperLog(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    public ResultMsg resetPwdSave(@RequestBody SysUser user)
    {
        user.setSalt(RandomUtil.randomStr(6));
        user.setPassword(PasswordUtil.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        return toAjax(sysUserService.resetUserPwd(user));
    }

    /**
     * 修改状态
     * @param sysUser
     * @return
     * @author zmr
     */
    @HasPermissions("system:user:edit")
    @PostMapping("status")
    @OperLog(title = "用户管理", businessType = BusinessType.UPDATE)
    public ResultMsg status(@RequestBody SysUser sysUser)
    {
        return toAjax(sysUserService.changeStatus(sysUser));
    }

    /**
     * 删除用户
     * @throws Exception 
     */
    @HasPermissions("system:user:remove")
    @OperLog(title = "用户管理", businessType = BusinessType.DELETE)
    @PostMapping("remove")
    public ResultMsg remove(String ids) throws Exception
    {
        return toAjax(sysUserService.deleteUserByIds(ids));
    }
}
