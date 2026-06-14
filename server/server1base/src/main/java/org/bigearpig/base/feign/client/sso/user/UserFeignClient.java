package org.bigearpig.base.feign.client.sso.user;

import javax.validation.Valid;

import org.bigearpig.base.feign.client.sso.user.mo.AddPermissionInnerMo;
import org.bigearpig.base.feign.client.sso.user.vo.PermissionInnerVo;
import org.bigearpig.common.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@FeignClient(name = "server2sso", contextId = "user", path = "/user")
public interface UserFeignClient {

	@PostMapping(value = "/addPermissionInner")
	public ResultData<PermissionInnerVo> addPermissionInner(@RequestBody @Valid AddPermissionInnerMo addPermissionInnerMo);
}
