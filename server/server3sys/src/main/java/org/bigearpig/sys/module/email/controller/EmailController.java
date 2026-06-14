package org.bigearpig.sys.module.email.controller;

import cn.hutool.core.bean.BeanUtil;


import org.bigearpig.base.feign.Inner;
import org.bigearpig.base.feign.client.sys.email.mo.SendEmailInnerMo;
import org.bigearpig.base.feign.client.sys.email.vo.EmailRecordInnerVo;
import org.bigearpig.base.mybatis.BasePageVo;
import org.bigearpig.common.ResultData;
import org.bigearpig.sys.module.email.controller.mo.SendEmailMo;
import org.bigearpig.sys.module.email.controller.mo.UpdateEmailTemplateMo;
import org.bigearpig.sys.module.email.controller.qo.EmailRecordPageQo;
import org.bigearpig.sys.module.email.controller.qo.EmailTemplatePageQo;
import org.bigearpig.sys.module.email.controller.vo.EmailRecordVo;
import org.bigearpig.sys.module.email.controller.vo.EmailTemplateVo;
import org.bigearpig.sys.module.email.db.entity.EmailRecordEntity;
import org.bigearpig.sys.module.email.controller.mo.AddEmailTemplateMo;
import org.bigearpig.sys.module.email.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping(value = "/email")
public class EmailController {
	
	@Autowired
	private EmailService emailService;

	@PostMapping("/addEmailTemplate")
	@PreAuthorize(value = "hasAnyAuthority('sys:email:addEmailTemplate')")
	public ResultData<EmailTemplateVo> addEmailTemplate(@RequestBody @Valid AddEmailTemplateMo addEmailTemplateMo) {
		return ResultData.setObj(emailService.addEmailTemplate(addEmailTemplateMo));
	}

	@PostMapping("/delEmailTemplate")
	@PreAuthorize(value = "hasAnyAuthority('sys:email:delEmailTemplate')")
	public ResultData<Boolean> delEmailTemplate(@RequestParam Long tableId) {
		return ResultData.setObj(emailService.delEmailTemplate(tableId));
	}

	@PostMapping("/updateEmailTemplate")
	@PreAuthorize(value = "hasAnyAuthority('sys:email:updateEmailTemplate')")
	public ResultData<EmailTemplateVo> updateEmailTemplate(
			@RequestBody @Valid UpdateEmailTemplateMo updateEmailTemplateMo) {
		return ResultData.setObj(emailService.updateEmailTemplate(updateEmailTemplateMo));
	}

	@PostMapping("/queryPageEmailTemplate")
	public ResultData<BasePageVo<EmailRecordVo>> queryPageEmailTemplate(
			@RequestBody @Valid EmailTemplatePageQo emailTemplatePageQo) {
		return ResultData.setObj(emailService.queryPageEmailTemplate(emailTemplatePageQo));
	}

	@PostMapping("/sendEmail")
	public ResultData<EmailRecordVo> sendEmail(@RequestBody SendEmailMo sendEmailMo) {
		return ResultData.setObj(emailService.sendEmail(sendEmailMo));
	}

	@Inner
	@PostMapping("/sendEmailInner")
	public ResultData<EmailRecordInnerVo> sendEmailInner(@RequestBody @Valid SendEmailInnerMo sendEmailInnerMo) {
		EmailRecordEntity emailRecordEntity = emailService.sendEmailByEmailTemplateCode(
				sendEmailInnerMo.getEmailTemplateCode(), sendEmailInnerMo.getAddrTo(), sendEmailInnerMo.getParamMap());
		EmailRecordInnerVo vo = new EmailRecordInnerVo();
		BeanUtil.copyProperties(emailRecordEntity, vo);
		return ResultData.setObj(vo);
	};

	@PostMapping("/queryPageEmailRecord")
	public ResultData<BasePageVo<EmailRecordVo>> queryPageEmailRecord(
			@RequestBody @Valid EmailRecordPageQo emailRecordPageQo) {
		return ResultData.setObj(emailService.queryPageEmailRecord(emailRecordPageQo));
	}
}
