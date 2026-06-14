package org.bigearpig.sys.module.seq.db.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bigearpig.sys.module.seq.db.entity.SeqEntity;
import org.bigearpig.sys.module.seq.db.mapper.SeqEntityMapper;
import org.bigearpig.sys.module.seq.db.service.SeqEntityService;

@Service
public class SeqEntityServiceImpl extends ServiceImpl<SeqEntityMapper, SeqEntity> implements SeqEntityService {

}
