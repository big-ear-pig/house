package org.bigearpig.sso.module.user.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.bigearpig.common.ResultData;
import org.springframework.web.bind.annotation.*;
import org.bigearpig.common.UserInfo;
import org.bigearpig.sso.module.user.controller.mo.LoginMo;
import org.bigearpig.sso.module.user.service.UserService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserRestController {
    @Resource
    private UserService userService;



    @SentinelResource
    @GetMapping("/login")
    public ResultData<String> login(LoginMo mo) {
        return ResultData.setObj(userService.login(mo));
    }
    @SentinelResource
    @PostMapping("/info")
    public ResultData<UserInfo> info(){
        return ResultData.setObj(userService.info());
    }


}
