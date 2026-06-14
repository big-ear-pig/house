package org.bigearpig.sso.module.dic.db.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bigearpig.sso.module.dic.db.entity.DicEntity;
import org.bigearpig.sso.module.dic.db.mapper.DicEntityMapper;
import org.bigearpig.sso.module.dic.db.service.DicEntityService;

@Service
public class DicEntityServiceImpl extends ServiceImpl<DicEntityMapper, DicEntity> implements DicEntityService {

}
