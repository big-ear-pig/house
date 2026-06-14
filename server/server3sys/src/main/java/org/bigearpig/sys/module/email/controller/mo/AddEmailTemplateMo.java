package org.bigearpig.sys.module.email.controller.mo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AddEmailTemplateMo {


	private String emailTemplateCode;

	private String subject;

	private String content;
	
	private List<AddEmailTemplateParamMo> paramList;

}
