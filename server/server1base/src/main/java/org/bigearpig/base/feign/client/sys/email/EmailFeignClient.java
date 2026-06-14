package org.bigearpig.base.feign.client.sys.email;


import org.bigearpig.base.feign.client.sys.email.mo.SendEmailInnerMo;
import org.bigearpig.base.feign.client.sys.email.vo.EmailRecordInnerVo;
import org.bigearpig.common.ResultData;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


@FeignClient(name = "server3sys", contextId = "email", path = "/email")
public interface EmailFeignClient {


    @PostMapping(value = "/sendEmailInner")
    ResultData<EmailRecordInnerVo> sendEmailInner(@RequestBody @Valid SendEmailInnerMo sendEmailInnerMo);




}
