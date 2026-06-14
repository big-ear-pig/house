package org.bigearpig.base.feign.client.sso.dic;

import org.bigearpig.base.feign.client.sso.dic.mo.AddDicInnerMo;
import org.bigearpig.base.feign.client.sso.dic.vo.DicItemInnerVo;
import org.bigearpig.common.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import javax.validation.Valid;

@FeignClient(name = "server2sso", contextId = "dic", path = "/dic")
public interface DicFeignClient {

    @PostMapping("/addDicInner")
    public ResultData<DicItemInnerVo> addDicInner(@RequestBody @Valid AddDicInnerMo addDicInnerMo);


}
