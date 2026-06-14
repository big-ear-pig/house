package org.bigearpig.base.feign.client.sso.user.mo;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AddPermissionInnerMo {

	@NotBlank(message = "权限名称不能为空")
	private String perName;

	private String perDescription;
	@NotBlank(message = "微服务名称不能为空")
	private String codeSys;
	@NotBlank(message = "模块名称不能为空")
	private String codeModule;
	@NotBlank(message = "方法名称不能为空")
	private String codeMethod;
}
