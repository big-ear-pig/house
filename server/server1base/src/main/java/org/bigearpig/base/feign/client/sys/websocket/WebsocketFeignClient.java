package org.bigearpig.base.feign.client.sys.websocket;



import javax.validation.Valid;

import org.bigearpig.base.feign.client.sys.websocket.mo.SendWsMessageMo;
import org.bigearpig.common.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "server3sys", contextId = "websocket", path = "/websocket")
public interface WebsocketFeignClient {

	@PostMapping(value = "/sendWsOneMessage")
    ResultData<Boolean> sendWsOneMessage(@RequestBody @Valid SendWsMessageMo sendWsMessageMo);
}
