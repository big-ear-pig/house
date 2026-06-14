package org.bigearpig.sso.module.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.bigearpig.base.security.SecurityUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.bigearpig.common.UserInfo;
import org.bigearpig.base.security.SecurityUserInfo;

import org.bigearpig.sso.module.user.controller.mo.LoginMo;
import org.bigearpig.sso.module.user.service.UserService;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String login(LoginMo mo) {
        String token = UUID.randomUUID().toString().replaceAll("-","");
        SecurityUserInfo securityUserInfo = new SecurityUserInfo();
        securityUserInfo.setTableId(Long.valueOf(mo.getUserName()));
        securityUserInfo.setUserName(mo.getUserName());
        securityUserInfo.setPassWord(mo.getPassWord());
        securityUserInfo.setToken(token);
        stringRedisTemplate.opsForValue().set(token, JSONObject.toJSONString(securityUserInfo),60*60, TimeUnit.SECONDS);
        return token;
    }

    @Override
    public UserInfo info() {
        return SecurityUtil.getUserDetails();
    }
}
