package org.bigearpig.sso.module.dic.db.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bigearpig.sso.module.dic.db.entity.DicItemEntity;
import org.bigearpig.sso.module.dic.db.mapper.DicItemEntityMapper;
import org.bigearpig.sso.module.dic.db.service.DicItemEntityService;

@Service
public class DicItemEntityServiceImpl extends ServiceImpl<DicItemEntityMapper, DicItemEntity>implements DicItemEntityService {

}
