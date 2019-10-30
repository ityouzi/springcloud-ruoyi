package com.ityouzi.system.controller;


import com.ityouzi.auth.annotation.HasPermissions;
import com.ityouzi.core.core.controller.BaseController;
import com.ityouzi.core.core.domain.ResultMsg;
import com.ityouzi.log.annotation.OperLog;
import com.ityouzi.log.enums.BusinessType;
import com.ityouzi.system.domain.SysDictData;
import com.ityouzi.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典数据 提供者
 * 
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("dict/data")
public class SysDictDataController extends BaseController
{
	
	@Autowired
	private ISysDictDataService sysDictDataService;
	
	/**
	 * 查询字典数据
	 */
	@GetMapping("get/{dictCode}")
	public SysDictData get(@PathVariable("dictCode") Long dictCode)
	{
		return sysDictDataService.selectDictDataById(dictCode);
		
	}
	
	/**
	 * 查询字典数据列表
	 */
	@GetMapping("list")
	@HasPermissions("system:dict:list")
	public ResultMsg list(SysDictData sysDictData)
	{
		startPage();
        return result(sysDictDataService.selectDictDataList(sysDictData));
	}
	
	/**
     * 根据字典类型查询字典数据信息
     * 
     * @param dictType 字典类型
     * @return 参数键值
     */
	@GetMapping("type")
    public List<SysDictData> getType(String dictType)
    {
        return sysDictDataService.selectDictDataByType(dictType);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     * 
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
	@GetMapping("label")
    public String getLabel(String dictType, String dictValue)
    {
        return sysDictDataService.selectDictLabel(dictType, dictValue);
    }
	
	
	/**
	 * 新增保存字典数据
	 */
	@OperLog(title = "字典数据", businessType = BusinessType.INSERT)
    @HasPermissions("system:dict:add")
	@PostMapping("save")
	public ResultMsg addSave(@RequestBody SysDictData sysDictData)
	{		
		return toAjax(sysDictDataService.insertDictData(sysDictData));
	}

	/**
	 * 修改保存字典数据
	 */
	@OperLog(title = "字典数据", businessType = BusinessType.UPDATE)
    @HasPermissions("system:dict:edit")
	@PostMapping("update")
	public ResultMsg editSave(@RequestBody SysDictData sysDictData)
	{		
		return toAjax(sysDictDataService.updateDictData(sysDictData));
	}
	
	/**
	 * 删除字典数据
	 */
	@OperLog(title = "字典数据", businessType = BusinessType.DELETE)
    @HasPermissions("system:dict:remove")
	@PostMapping("remove")
	public ResultMsg remove(String ids)
	{		
		return toAjax(sysDictDataService.deleteDictDataByIds(ids));
	}
	
}
