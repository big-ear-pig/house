package org.bigearpig.sso.module.dic.controller;

import org.bigearpig.base.feign.Inner;
import org.bigearpig.base.feign.client.sso.dic.mo.AddDicInnerMo;
import org.bigearpig.base.feign.client.sso.dic.vo.DicItemInnerVo;
import org.bigearpig.base.mybatis.BasePageVo;
import org.bigearpig.sso.module.dic.controller.mo.AddDicItemMo;
import org.bigearpig.sso.module.dic.controller.mo.AddDicMo;
import org.bigearpig.sso.module.dic.controller.mo.UpdateDicItemMo;
import org.bigearpig.sso.module.dic.controller.mo.UpdateDicMo;
import org.bigearpig.sso.module.dic.controller.qo.DicItemQo;
import org.bigearpig.sso.module.dic.controller.qo.DicQo;
import org.bigearpig.sso.module.dic.controller.vo.DicItemVo;
import org.bigearpig.sso.module.dic.controller.vo.DicVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.bigearpig.sso.module.dic.service.DicService;

import lombok.AllArgsConstructor;
import org.bigearpig.common.ResultData;


import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/dic")
@AllArgsConstructor
public class DicRestController {

	private DicService dicService;

	@Inner
	@PostMapping(value = "/addDicInner")
	public ResultData<DicItemInnerVo> addDicInner(@RequestBody @Valid AddDicInnerMo addDicInnerMo){
		return ResultData.setObj(dicService.addDicInner(addDicInnerMo));
	}


	@PostMapping(value = "/addDic")
	@PreAuthorize(value = "hasAnyAuthority('manage:dic:addDic')")
	public ResultData<DicVo> addDic(@RequestBody @Valid AddDicMo addDicMo) {
		return ResultData.setObj(dicService.addDic(addDicMo));
	}

	

	@PostMapping(value = "/delDic")
	@PreAuthorize(value = "hasAnyAuthority('manage:dic:delDic')")
	public ResultData<Boolean> delDic(@RequestParam Long tableId) {
		return ResultData.setObj(dicService.delDic(tableId));
	}

	

	@PostMapping(value = "/updateDic")
	@PreAuthorize(value = "hasAnyAuthority('manage:dic:updateDic')")
	public ResultData<DicVo> updateDic(@RequestBody @Valid UpdateDicMo updateDicMo) {
		return ResultData.setObj(dicService.updateDic(updateDicMo));
	}

	

	@PostMapping(value = "/queryListDic")
	@PreAuthorize(value = "hasAnyAuthority('manage:dic:queryListDic')")
	public ResultData<List<DicVo>> queryListDic(@RequestBody @Valid DicQo dicQo) {
		return ResultData.setObj(dicService.queryListDic(dicQo));
	}

	

	@PostMapping(value = "/queryPageDic")
	@PreAuthorize(value = "hasAnyAuthority('manage:dic:queryPageDic')")
	public ResultData<BasePageVo<DicVo>> queryPageDic(@RequestBody @Valid DicQo dicQo) {
		return ResultData.setObj(dicService.queryPageDic(dicQo));
	}

	

	@PostMapping(value = "/getDicById")
	@PreAuthorize(value = "hasAnyAuthority('manage:dic:getDicById')")
	public ResultData<DicVo> getDicById(@RequestParam Long tableId) {
		return ResultData.setObj(dicService.getDicById(tableId));
	}


	@PostMapping(value = "/addDicItem")
	@PreAuthorize(value = "hasAnyAuthority('manage:dic:addDicItem')")
	public ResultData<DicItemVo> addDicItem(@RequestBody @Valid AddDicItemMo addDicItemMo) {
		return ResultData.setObj(dicService.addDicItem(addDicItemMo));
	}


	@PostMapping(value = "/delDicItem")
	@PreAuthorize(value = "hasAnyAuthority('manage:dic:delDicItem')")
	public ResultData<Boolean> delDicItem(@RequestParam Long tableId) {
		return ResultData.setObj(dicService.delDicItem(tableId));
	}

	

	@PostMapping(value = "/updateDicItem")
	@PreAuthorize(value = "hasAnyAuthority('manage:dic:updateDicItem')")
	public ResultData<DicItemVo> updateDicItem(@RequestBody @Valid UpdateDicItemMo updateDicItemMo) {
		return ResultData.setObj(dicService.updateDicItem(updateDicItemMo));
	}

	

	@PostMapping(value = "/queryListDicItem")
	@PreAuthorize(value = "hasAnyAuthority('manage:dic:queryListDicItem')")
	public ResultData<List<DicItemVo>> queryListDicItem(@RequestBody @Valid DicItemQo dicItemQo) {
		return ResultData.setObj(dicService.queryListDicItem(dicItemQo));
	}


	

	@PostMapping(value = "/queryPageDicItem")
	@PreAuthorize(value = "hasAnyAuthority('manage:dic:queryPageDicItem')")
	public ResultData<BasePageVo<DicItemVo>> queryPageDicItem(@RequestBody @Valid DicItemQo dicItemQo){
		return ResultData.setObj(null);
	}


	

	@PostMapping(value = "/getAllDicDetail")
	public ResultData<List<DicVo>> getAllDicDetail() {
		List<DicVo> result = dicService.getAllDicDetail();
		return ResultData.setObj(result);
	}

	

	@PostMapping(value = "/getDicByCode")
	public ResultData<DicVo> getDicByCode(@RequestParam String dicCode) {
		return ResultData.setObj(dicService.getDicByCode(dicCode));
	}

	

	@PostMapping(value = "/getValueByCode")
	public ResultData<String> getValueByCode(@RequestParam String dicCode, @RequestParam String itemCode) {
		return ResultData.setObj(dicService.getValueByCode(dicCode, itemCode));
	}

}
