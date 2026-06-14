package org.bigearpig.sys.module.email.runner;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import org.bigearpig.sys.module.email.controller.mo.AddEmailTemplateMo;
import org.bigearpig.sys.module.email.controller.mo.AddEmailTemplateParamMo;
import org.bigearpig.sys.module.email.service.EmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmailTemplateRunner implements ApplicationRunner {
	
	@Autowired
	private EmailService emailService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		AddEmailTemplateMo addEmailTemplateMo =new AddEmailTemplateMo();
		addEmailTemplateMo.setEmailTemplateCode("emailCaptcha");
		addEmailTemplateMo.setSubject("邮件验证码");
		addEmailTemplateMo.setContent("<!DOCTYPE html>\r\n" + 
				"<html>\r\n" + 
				"<head>\r\n" + 
				"<meta charset=\"UTF-8\">\r\n" + 
				"<title>${title}</title>\r\n" + 
				"</head>\r\n" + 
				"<body>\r\n" + 
				"<img alt=\"\" src=\"${emailCaptcha}\">\r\n" + 
				"</body>\r\n" + 
				"</html>");
		List<AddEmailTemplateParamMo> list = new ArrayList<AddEmailTemplateParamMo>();
		AddEmailTemplateParamMo mo = new AddEmailTemplateParamMo();
		mo.setParamLabel("emailCaptcha");
		list.add(mo);
		AddEmailTemplateParamMo mo1 = new AddEmailTemplateParamMo();
		mo1.setParamLabel("title");
		list.add(mo1);
		addEmailTemplateMo.setParamList(list);
		emailService.addEmailTemplate(addEmailTemplateMo);
	}

}
