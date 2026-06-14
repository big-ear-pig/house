package org.bigearpig.sys.module.user.controller;

import org.bigearpig.common.ResultData;
import org.bigearpig.common.UserInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {



    @RequestMapping("/getUserInfo")
    public ResultData<UserInfo> getUserInfo(){
        return null;
    }
}
