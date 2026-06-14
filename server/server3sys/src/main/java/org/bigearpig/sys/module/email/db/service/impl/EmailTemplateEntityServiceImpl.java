package org.bigearpig.sys.module.email.db.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bigearpig.sys.module.email.db.entity.EmailTemplateEntity;
import org.bigearpig.sys.module.email.db.mapper.EmailTemplateEntityMapper;
import org.bigearpig.sys.module.email.db.service.EmailTemplateEntityService;

@Service
public class EmailTemplateEntityServiceImpl extends ServiceImpl<EmailTemplateEntityMapper, EmailTemplateEntity> implements EmailTemplateEntityService {

}
