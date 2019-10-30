package com.ityouzi.system.controller;


import com.ityouzi.auth.annotation.HasPermissions;
import com.ityouzi.core.core.controller.BaseController;
import com.ityouzi.core.core.domain.ResultMsg;
import com.ityouzi.core.core.page.PageDomain;
import com.ityouzi.log.annotation.OperLog;
import com.ityouzi.log.enums.BusinessType;
import com.ityouzi.system.domain.SysDictType;
import com.ityouzi.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 字典类型 提供者
 * 
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("dict/type")
public class SysDictTypeController extends BaseController
{
	
	@Autowired
	private ISysDictTypeService sysDictTypeService;
	
	/**
	 * 查询字典类型
	 */
	@GetMapping("get/{dictId}")
	public SysDictType get(@PathVariable("dictId") Long dictId)
	{
		return sysDictTypeService.selectDictTypeById(dictId);
		
	}
	
	/**
	 * 查询字典类型列表
	 */
	@GetMapping("list")
	@HasPermissions("system:dict:list")
	public ResultMsg list(SysDictType sysDictType, PageDomain page)
	{
		startPage();
        return result(sysDictTypeService.selectDictTypeList(sysDictType));
	}
	
	
	/**
	 * 新增保存字典类型
	 */
	@OperLog(title = "字典类型", businessType = BusinessType.INSERT)
    @HasPermissions("system:dict:add")
	@PostMapping("save")
	public ResultMsg addSave(@RequestBody SysDictType sysDictType)
	{		
		return toAjax(sysDictTypeService.insertDictType(sysDictType));
	}

	/**
	 * 修改保存字典类型
	 */
	@OperLog(title = "字典类型", businessType = BusinessType.UPDATE)
    @HasPermissions("system:dict:edit")
	@PostMapping("update")
	public ResultMsg editSave(@RequestBody SysDictType sysDictType)
	{		
		return toAjax(sysDictTypeService.updateDictType(sysDictType));
	}
	
	/**
	 * 删除字典类型
	 * @throws Exception 
	 */
	@OperLog(title = "字典类型", businessType = BusinessType.DELETE)
	@HasPermissions("system:dict:remove")
	@PostMapping("remove")
	public ResultMsg remove(String ids) throws Exception
	{		
		return toAjax(sysDictTypeService.deleteDictTypeByIds(ids));
	}
	
}
