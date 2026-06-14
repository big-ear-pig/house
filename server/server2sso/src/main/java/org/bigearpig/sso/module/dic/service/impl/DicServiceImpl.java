package org.bigearpig.sso.module.dic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.bigearpig.base.feign.client.sso.dic.mo.AddDicItemInnerMo;
import org.bigearpig.sso.module.dic.db.entity.DicEntity;
import org.bigearpig.sso.module.dic.db.entity.DicItemEntity;
import org.bigearpig.sso.module.dic.db.entity.DicTypeEnum;
import org.bigearpig.sso.module.dic.controller.mo.AddDicItemMo;
import org.bigearpig.sso.module.dic.controller.mo.AddDicMo;
import org.bigearpig.sso.module.dic.controller.mo.UpdateDicItemMo;
import org.bigearpig.sso.module.dic.controller.mo.UpdateDicMo;
import org.bigearpig.sso.module.dic.controller.qo.DicItemQo;
import org.bigearpig.sso.module.dic.controller.qo.DicQo;
import org.bigearpig.sso.module.dic.controller.vo.DicItemVo;
import org.bigearpig.sso.module.dic.controller.vo.DicVo;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import org.bigearpig.base.feign.client.sso.dic.mo.AddDicInnerMo;
import org.bigearpig.base.feign.client.sso.dic.vo.DicItemInnerVo;
import org.bigearpig.base.mybatis.BasePageVo;
import org.bigearpig.sso.module.dic.db.service.DicEntityService;
import org.bigearpig.sso.module.dic.db.service.DicItemEntityService;
import org.bigearpig.sso.module.dic.mapper.DicMapper;
import org.bigearpig.sso.module.dic.service.DicService;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DicServiceImpl implements DicService {
	@Resource
	private DicEntityService dicEntityService;
	@Resource
	private DicItemEntityService dicItemEntityService;
	@Resource
	private DicMapper dicMapper;
	@Resource
	@Lazy
	private DicService dicService;

	@Override
	public DicItemInnerVo addDicInner(AddDicInnerMo addDicInnerMo) {
		QueryWrapper<DicEntity> dicEntityQueryWrapper = new QueryWrapper<>();
		dicEntityQueryWrapper.lambda().eq(DicEntity::getDicCode, addDicInnerMo.getDicCode());
		DicEntity dicEntity = dicEntityService.getOne(dicEntityQueryWrapper);
		if (ObjectUtil.isNull(dicEntity)) {
			dicEntity = new DicEntity();
			BeanUtil.copyProperties(addDicInnerMo,dicEntity);
			dicEntity.setDicType(DicTypeEnum.SYSTEMENUM.getItemCode());
			dicEntityService.save(dicEntity);
			DicItemEntity dicItemEntity = new DicItemEntity();
			dicItemEntity.setDicId(dicEntity.getTableId());
			dicItemEntity.setItemCode(DicTypeEnum.UNKNOW.getItemCode());
			dicItemEntity.setItemValue(DicTypeEnum.UNKNOW.getItemValue());
			dicItemEntity.setShowFlag(false);
			dicItemEntity.setCheckFlag(false);
			dicItemEntityService.save(dicItemEntity);
		}else{
			if(StrUtil.equals(DicTypeEnum.SYSTEMENUM.getItemCode(),dicEntity.getDicType())){

			}else{
				dicEntity.setDicType(DicTypeEnum.SYSTEMENUM.getItemCode());
				dicEntityService.updateById(dicEntity);
			}

		}
		for(AddDicItemInnerMo mo : addDicInnerMo.getItemList()){
			QueryWrapper<DicItemEntity> dicItemEntityQueryWrapper = new QueryWrapper<>();
			dicItemEntityQueryWrapper.lambda().eq(DicItemEntity::getDicId, dicEntity.getTableId())
					.eq(DicItemEntity::getItemCode,mo.getItemCode());
			DicItemEntity dicItemEntity = dicItemEntityService.getOne(dicItemEntityQueryWrapper);
			if(ObjectUtil.isNull(dicItemEntity)){
				dicItemEntity = new DicItemEntity();
				dicItemEntity.setDicId(dicEntity.getTableId());
				dicItemEntity.setItemCode(mo.getItemCode());
				dicItemEntity.setItemValue(mo.getItemValue());
				dicItemEntity.setShowFlag(mo.getShowFlag());
				dicItemEntity.setCheckFlag(mo.getCheckFlag());
				dicItemEntityService.save(dicItemEntity);
			}else{
				dicItemEntity.setItemValue(mo.getItemValue());
				dicItemEntityService.updateById(dicItemEntity);
			}

		}
		flushGetAllDicDetailCache();
		return null;
	}

	@Override
	public DicVo addDic(AddDicMo addDicMo) {
		QueryWrapper<DicEntity> dicEntityQueryWrapper = new QueryWrapper<>();
		dicEntityQueryWrapper.lambda().eq(DicEntity::getDicCode, addDicMo.getDicCode());
		DicEntity dicEntity = dicEntityService.getOne(dicEntityQueryWrapper);

		if (ObjectUtil.isNotNull(dicEntity)) {
			throw new RuntimeException("已经存在" + addDicMo.getDicCode());
		}
		dicEntity = new DicEntity();
		BeanUtil.copyProperties(addDicMo, dicEntity);
		dicEntity.setDicCode(DicTypeEnum.DICTYPE.getItemCode());
		boolean addFlag = dicEntityService.save(dicEntity);
		if (addFlag) {
			DicItemEntity dicItemEntity = new DicItemEntity();
			dicItemEntity.setDicId(dicEntity.getTableId());
			dicItemEntity.setItemCode(DicTypeEnum.UNKNOW.getItemCode());
			dicItemEntity.setItemValue(DicTypeEnum.UNKNOW.getItemValue());
			dicItemEntity.setShowFlag(false);
			dicItemEntity.setCheckFlag(false);
			dicItemEntityService.save(dicItemEntity);
			flushGetAllDicDetailCache();
			DicVo dicVO = new DicVo();
			BeanUtil.copyProperties(dicEntity, dicVO);
			return dicVO;
		} else {
			throw new RuntimeException("新增dic失败");
		}

	}

	@Override
	public Boolean delDic(Long id) {
		DicEntity dicEntity = dicEntityService.getById(id);
		if(ObjectUtil.isNotNull(dicEntity)){
			if(StrUtil.equals(DicTypeEnum.DICTYPE.getItemCode(),dicEntity.getDicType()) ){
				QueryWrapper<DicItemEntity> dicItemEntityQueryWrapper = new QueryWrapper<>();
				dicItemEntityQueryWrapper.lambda().eq(DicItemEntity::getDicId,id);
				dicItemEntityService.remove(dicItemEntityQueryWrapper);
				dicEntityService.removeById(id);
				flushGetAllDicDetailCache();
				return true;
			}else{
				throw new RuntimeException("数据有问题");
			}
		}else{
			throw new RuntimeException("数据有问题");
		}

	}

	@Override
	public DicVo updateDic(UpdateDicMo updateDicMo) {
		flushGetAllDicDetailCache();
		return null;
	}

	@Override
	public List<DicVo> queryListDic(DicQo dicQo) {
		QueryWrapper<DicEntity> dicEntityQueryWrapper = changeDicQuery(dicQo);
		List<DicEntity> list = dicEntityService.list(dicEntityQueryWrapper);
		return list.stream().map(dicEntity -> {
			DicVo vo = new DicVo();
			BeanUtil.copyProperties(dicEntity, vo);
			return vo;
		}).collect(Collectors.toList());
	}

	@Override
	public BasePageVo<DicVo> queryPageDic(DicQo dicQo) {
		Page<DicEntity> page = null;
		QueryWrapper<DicEntity> dicEntityQueryWrapper = null;
		page = dicEntityService.page(page, dicEntityQueryWrapper);
		BasePageVo<DicEntity> basePageVo =  BasePageVo.build(dicQo,page.getTotal(),page.getRecords());
		return BasePageVo.convert(basePageVo,(dicEntity)->{
			DicVo vo = new DicVo();
			BeanUtil.copyProperties(dicEntity,vo);
			return vo;
		});
	}

	@Override
	public DicVo getDicById(Long id) {
		DicEntity dicEntity = dicEntityService.getById(id);
		if (ObjectUtil.isNotNull(dicEntity)) {
			DicVo vo = new DicVo();
			BeanUtil.copyProperties(dicEntity, vo);
			return vo;
		} else {
			throw new RuntimeException("无法找到数据");
		}
	}

	@Override
	public DicItemVo addDicItem(AddDicItemMo addDicItemMo) {

		DicEntity dicEntity = dicEntityService.getById(addDicItemMo.getDicId());
		if (ObjectUtil.isNotNull(dicEntity)) {
			QueryWrapper<DicItemEntity> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().eq(DicItemEntity::getDicId, dicEntity.getTableId()).eq(DicItemEntity::getItemCode,
					addDicItemMo.getItemCode());
			DicItemEntity dicItemEntity = dicItemEntityService.getOne(queryWrapper);
			if (ObjectUtil.isNotNull(dicItemEntity)) {
				throw new RuntimeException(dicEntity.getDicCode() + "下" + dicItemEntity.getItemCode() + "已经存在");
			} else {
				dicItemEntity = new DicItemEntity();
				BeanUtil.copyProperties(addDicItemMo, dicItemEntity);
				boolean flag = dicItemEntityService.save(dicItemEntity);
				if (flag) {
					flushGetAllDicDetailCache();
					DicItemVo vo = new DicItemVo();
					BeanUtil.copyProperties(dicItemEntity, vo);
					return vo;
				} else {
					throw new RuntimeException("");
				}
			}

		} else {
			throw new RuntimeException("");
		}
	}

	@Override
	public Boolean delDicItem(Long id) {
		flushGetAllDicDetailCache();
		return null;
	}

	@Override
	public DicItemVo updateDicItem(UpdateDicItemMo updateDicItemMo) {
		flushGetAllDicDetailCache();
		return null;
	}

	@Override
	public List<DicItemVo> queryListDicItem(DicItemQo dicItemQo) {
		QueryWrapper<DicItemEntity> dicItemEntityQueryWrapper = changeDicItemQuery(dicItemQo);

		List<DicItemEntity> list = dicItemEntityService.list(dicItemEntityQueryWrapper);

		return list.stream().map(dicItemEntity -> {
			DicItemVo vo = new DicItemVo();
			BeanUtil.copyProperties(dicItemEntity, vo);
			return vo;
		}).collect(Collectors.toList());

	}

	private QueryWrapper<DicItemEntity> changeDicItemQuery(DicItemQo dicItemQo) {
		QueryWrapper<DicItemEntity> dicItemEntityQueryWrapper = new QueryWrapper<>();
		if (ObjectUtil.isNotNull(dicItemQo.getDicId())) {
			dicItemEntityQueryWrapper.lambda().eq(DicItemEntity::getDicId, dicItemQo.getDicId());
		}
		return dicItemEntityQueryWrapper;
	}

	private QueryWrapper<DicEntity> changeDicQuery(DicQo dicQo) {
		QueryWrapper<DicEntity> dicEntityQueryWrapper = new QueryWrapper<>();
		if (StrUtil.isNotBlank(dicQo.getDicName())) {
			dicEntityQueryWrapper.lambda().like(DicEntity::getDicName, dicQo.getDicName());
		}
		if (StrUtil.isNotBlank(dicQo.getDicCode())) {
			dicEntityQueryWrapper.lambda().like(DicEntity::getDicCode, dicQo.getDicCode());
		}

		return dicEntityQueryWrapper;
	}

	@Override
	public List<DicVo> getAllDicDetail() {
		List<DicEntity> dicEntityList = dicEntityService.list();
		if (ObjectUtil.isNotEmpty(dicEntityList)) {
			return dicEntityList.stream().map(dicEntity -> {
				DicVo dicVo = new DicVo();
				dicVo.setDicName(dicEntity.getDicName());
				dicVo.setDicCode(dicEntity.getDicCode());
				QueryWrapper<DicItemEntity> dicItemEntityQueryWrapper = new QueryWrapper<>();
				dicItemEntityQueryWrapper.lambda().eq(DicItemEntity::getDicId, dicEntity.getTableId());
				List<DicItemEntity> dicItemEntityList = dicItemEntityService.list(dicItemEntityQueryWrapper);
				if (ObjectUtil.isNotEmpty(dicItemEntityList)) {
					dicVo.setDicItemVoList(dicItemEntityList.stream().map(dicItemEntity -> {
						DicItemVo dicItemVo = new DicItemVo();
						dicItemVo.setItemCode(dicItemEntity.getItemCode());
						dicItemVo.setItemValue(dicItemEntity.getItemValue());
						dicItemVo.setShowFlag(dicItemEntity.getShowFlag());
						dicItemVo.setCheckFlag(dicItemEntity.getCheckFlag());
						return dicItemVo;
					}).collect(Collectors.toList()));
				} else {
					dicVo.setDicItemVoList(new ArrayList<>());
				}
				return dicVo;
			}).collect(Collectors.toList());
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public DicVo getDicByCode(String dicCode) {
		List<DicVo> list = getAllDicDetail();
		if (ObjectUtil.isNotEmpty(list)) {
			for (DicVo dicVo : list) {
				if (StrUtil.equals(dicVo.getDicCode(), dicCode)) {
					return dicVo;
				}
			}
		}
		throw new RuntimeException("");
	}

	@Override
	public String getValueByCode(String dicCode, String itemCode) {
		DicVo vo = getDicByCode(dicCode);
		if (ObjectUtil.isNotEmpty(vo.getDicItemVoList())) {
			for (DicItemVo itemVo : vo.getDicItemVoList()) {
				if (StrUtil.equals(itemVo.getItemCode(), itemCode)) {
					return itemVo.getItemValue();
				}
			}
		}
		throw new RuntimeException("");
	}

	private void flushGetAllDicDetailCache() {
		dicService.delGetAllDicDetailCache();
		dicService.getAllDicDetail();
	}
	
	@Override
	@CacheEvict(value = "getAllDicDetail")
	public void delGetAllDicDetailCache() {

	}
	
}
