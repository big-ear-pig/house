package org.bigearpig.sso.module.dic.service;


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


import java.util.List;

public interface DicService {

	DicItemInnerVo addDicInner(AddDicInnerMo addDicInnerMo);

	DicVo addDic(AddDicMo addDicMo);

	Boolean delDic(Long id);

	DicVo updateDic(UpdateDicMo updateDicMo);

	List<DicVo> queryListDic(DicQo dicQo);

	BasePageVo<DicVo> queryPageDic(DicQo dicQo);

	DicVo getDicById(Long id);

	DicItemVo addDicItem(AddDicItemMo addDicItemMo);

	Boolean delDicItem(Long id);

	DicItemVo updateDicItem(UpdateDicItemMo updateDicItemMo);

	List<DicItemVo> queryListDicItem(DicItemQo dicItemQo);

	List<DicVo> getAllDicDetail();

	DicVo getDicByCode(String dicCode);

	String getValueByCode(String dicCode, String itemCode);
	
	void delGetAllDicDetailCache();
}
