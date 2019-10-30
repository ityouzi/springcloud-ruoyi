package com.ityouzi.system.controller;


import com.ityouzi.core.core.controller.BaseController;
import com.ityouzi.core.core.domain.ResultMsg;
import com.ityouzi.system.domain.SysConfig;
import com.ityouzi.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 参数配置 提供者
 * 
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("config")
public class SysConfigController extends BaseController
{
	
	@Autowired
	private ISysConfigService sysConfigService;
	
	/**
	 * 查询参数配置
	 */
	@GetMapping("get/{configId}")
	public SysConfig get(@PathVariable("configId") Long configId)
	{
		return sysConfigService.selectConfigById(configId);
		
	}
	
	/**
	 * 查询参数配置列表
	 */
	@GetMapping("list")
	public ResultMsg list(SysConfig sysConfig)
	{
		startPage();
        return result(sysConfigService.selectConfigList(sysConfig));
	}
	
	
	/**
	 * 新增保存参数配置
	 */
	@PostMapping("save")
	public ResultMsg addSave(@RequestBody SysConfig sysConfig)
	{
		return toAjax(sysConfigService.insertConfig(sysConfig));
	}

	/**
	 * 修改保存参数配置
	 */
	@PostMapping("update")
	public ResultMsg editSave(@RequestBody SysConfig sysConfig)
	{
		return toAjax(sysConfigService.updateConfig(sysConfig));
	}

	/**
	 * 删除参数配置
	 */
	@PostMapping("remove")
	public ResultMsg remove(String ids)
	{		
		return toAjax(sysConfigService.deleteConfigByIds(ids));
	}
	
}
