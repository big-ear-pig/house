package org.bigearpig.base.feign.client.sys.websocket.mo;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SendWsMessageMo {
	@NotBlank(message = "businessType不能为空")
	private String businessType = "";

	private Long fromUserId = -1L;

	private String fromUserName = "";

	private Long toUserId = -1L;

	private String toUserName = "";

	private String typePath = "";

	@NotBlank(message = "obj不能为空")
	private String obj = "";
}
