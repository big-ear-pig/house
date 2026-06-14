package org.bigearpig.sys.module.email.db.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bigearpig.sys.module.email.db.entity.EmailRecordEntity;
import org.bigearpig.sys.module.email.db.mapper.EmailRecordEntityMapper;
import org.bigearpig.sys.module.email.db.service.EmailRecordEntityService;

@Service
public class EmailRecordEntityServiceImpl extends ServiceImpl<EmailRecordEntityMapper, EmailRecordEntity> implements EmailRecordEntityService {

}
