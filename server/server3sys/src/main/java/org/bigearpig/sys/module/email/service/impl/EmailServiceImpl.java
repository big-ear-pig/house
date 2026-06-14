package org.bigearpig.sys.module.email.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.bigearpig.base.mybatis.BasePageVo;
import org.bigearpig.sys.module.email.db.entity.EmailRecordEntity;
import org.bigearpig.sys.module.email.db.entity.EmailTemplateEntity;
import org.bigearpig.sys.module.email.db.entity.EmailTemplateParamEntity;
import org.bigearpig.sys.module.email.db.service.EmailRecordEntityService;
import org.bigearpig.sys.module.email.db.service.EmailTemplateEntityService;
import org.bigearpig.sys.module.email.db.service.EmailTemplateParamEntityService;

import org.bigearpig.sys.module.email.component.EmailComponent;
import org.bigearpig.sys.module.email.mapper.EmailMapper;
import org.bigearpig.sys.module.email.controller.mo.AddEmailTemplateMo;
import org.bigearpig.sys.module.email.controller.mo.AddEmailTemplateParamMo;
import org.bigearpig.sys.module.email.controller.mo.SendEmailMo;
import org.bigearpig.sys.module.email.controller.mo.UpdateEmailTemplateMo;
import org.bigearpig.sys.module.email.controller.qo.EmailRecordPageQo;
import org.bigearpig.sys.module.email.controller.qo.EmailTemplatePageQo;
import org.bigearpig.sys.module.email.service.EmailService;
import org.bigearpig.sys.module.email.controller.vo.EmailRecordVo;
import org.bigearpig.sys.module.email.controller.vo.EmailTemplateVo;
import org.bigearpig.sys.module.file.service.FileService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.bigearpig.sys.module.template.component.FreemarkerTemplateComponent;


import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
	@Resource
	private EmailMapper emailMapper;
	@Resource
	private EmailComponent emailComponent;
	@Resource
	private EmailRecordEntityService emailRecordEntityService;
	@Resource
	private EmailTemplateEntityService emailTemplateEntityService;
	@Resource
	private EmailTemplateParamEntityService emailTemplateParamEntityService;
	@Resource
	private FileService fileService;
	@Resource
	private FreemarkerTemplateComponent freemarkerTemplateComponent;
	@Override
	public EmailTemplateVo addEmailTemplate(AddEmailTemplateMo addEmailTemplateMo) {
		QueryWrapper<EmailTemplateEntity> query = new QueryWrapper<EmailTemplateEntity>();
		query.lambda().eq(EmailTemplateEntity::getEmailTemplateCode, addEmailTemplateMo.getEmailTemplateCode());
		EmailTemplateEntity entity = emailTemplateEntityService.getOne(query);
		if (ObjectUtil.isNull(entity)) {
			entity = new EmailTemplateEntity();
			BeanUtil.copyProperties(addEmailTemplateMo, entity);
			emailTemplateEntityService.save(entity);
			if (ObjectUtil.isNotEmpty(addEmailTemplateMo.getParamList())) {
				for (AddEmailTemplateParamMo mo : addEmailTemplateMo.getParamList()) {
					EmailTemplateParamEntity param = new EmailTemplateParamEntity();
					BeanUtil.copyProperties(mo, param);
					param.setEmailTemplateId(entity.getTableId());
					emailTemplateParamEntityService.save(param);
				}
			}
		}
		EmailTemplateVo vo = new EmailTemplateVo();
		BeanUtil.copyProperties(entity, vo);
		return vo;
	}
	@Override
	public Boolean delEmailTemplate(Long tableId) {
		return true;
	}
	@Override
	public EmailTemplateVo updateEmailTemplate(UpdateEmailTemplateMo updateEmailTemplateMo) {
		return null;
	}
	
	@Override
	public BasePageVo<EmailRecordVo> queryPageEmailTemplate(EmailTemplatePageQo emailTemplatePageQo) {

		return null;
	}
	@Override
	public EmailRecordVo sendEmail(SendEmailMo sendEmailMo) {
		List<MultipartFile> imgList = new ArrayList<>();
		List<MultipartFile> fileList = new ArrayList<>();
		EmailRecordEntity emailRecordEntity = sendHtmlMailNoException(sendEmailMo.getTo(), sendEmailMo.getSubject(),
				sendEmailMo.getContent(), imgList, fileList);
		EmailRecordVo emailRecordVo = new EmailRecordVo();
		BeanUtil.copyProperties(emailRecordEntity, emailRecordVo);
		return emailRecordVo;
	}

	@Override
	public EmailRecordEntity sendEmailByEmailTemplateCode(String emailTemplateCode, String email,
			Map<String, Object> map) {
		QueryWrapper<EmailTemplateEntity> emailTemplateEntityQueryWrapper = new QueryWrapper<>();
		emailTemplateEntityQueryWrapper.lambda().eq(EmailTemplateEntity::getEmailTemplateCode, emailTemplateCode);
		EmailTemplateEntity emailTemplateEntity = emailTemplateEntityService.getOne(emailTemplateEntityQueryWrapper);
		if (ObjectUtil.isNotNull(emailTemplateEntity)) {
			QueryWrapper<EmailTemplateParamEntity> query = new QueryWrapper<EmailTemplateParamEntity>();
			query.lambda().eq(EmailTemplateParamEntity::getEmailTemplateId, emailTemplateEntity.getTableId());
			List<EmailTemplateParamEntity> list = emailTemplateParamEntityService.list(query);
			if (ObjectUtil.isNotEmpty(list)) {
				for (EmailTemplateParamEntity emailTemplateParamEntity : list) {
					Object obj = map.get(emailTemplateParamEntity.getParamLabel());
					if (ObjectUtil.isNull(obj)) {
						throw new RuntimeException("");
					}
				}
			}
			String htmlContent = freemarkerTemplateComponent.generateStringFreeMarkerTemplate(emailTemplateEntity.getContent(), map);
			return sendHtmlMailNoException(email, emailTemplateEntity.getSubject(), htmlContent,
					null, null);
		} else {
			throw new RuntimeException("");
		}

	}

	private EmailRecordEntity sendHtmlMailNoException(String to, String subject, String content,
			List<MultipartFile> imgList, List<MultipartFile> fileList) {
		EmailRecordEntity emailRecordEntity = new EmailRecordEntity();
		Boolean flag = emailComponent.sendHtmlMailNoException(to, subject, content, imgList, fileList);
		emailRecordEntity.setAddrTo(to);
		emailRecordEntity.setSubject(subject);
		emailRecordEntity.setContent(content);
		emailRecordEntity.setSendStatus(flag);
		emailRecordEntity.setSendTime(LocalDateTime.now());
		emailRecordEntityService.save(emailRecordEntity);
		return emailRecordEntity;
	}

	
	@Override
	public BasePageVo<EmailRecordVo> queryPageEmailRecord(EmailRecordPageQo emailRecordPageQo) {
		Page<EmailRecordEntity> page = null;
		QueryWrapper<EmailRecordEntity> query = null;
		page = emailRecordEntityService.page(page,query);
		return null;
	}


}
