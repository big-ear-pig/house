package org.bigearpig.sso.module.dic.controller.mo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddDicItemMo {
	@NotNull(message = "")
	private Long dicId;
	@NotBlank(message = "")
	private String itemCode;
	@NotBlank(message = "")
	private String itemValue;

	// 是否展示
	private Boolean showFlag = true;
	// 是否可以选择
	private Boolean checkFlag = true;

}
