package org.bigearpig.sys.module.email.db.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bigearpig.sys.module.email.db.entity.EmailTemplateParamEntity;
import org.bigearpig.sys.module.email.db.mapper.EmailTemplateParamEntityMapper;
import org.bigearpig.sys.module.email.db.service.EmailTemplateParamEntityService;

@Service
public class EmailTemplateParamEntityServiceImpl extends ServiceImpl<EmailTemplateParamEntityMapper, EmailTemplateParamEntity> implements EmailTemplateParamEntityService {

}
