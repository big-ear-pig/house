package org.bigearpig.sys.module.email.controller.vo;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

public class EmailTemplateVo {

	private String emailTemplateCode;

	private String subject;

	private String content;
}
