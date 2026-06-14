package org.bigearpig.base.security;

import cn.hutool.core.util.ObjectUtil;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


@Slf4j
public class SecurityUtil {

    public static SecurityUserInfo getUserDetails() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (ObjectUtil.isNotNull(securityContext)) {
            Authentication authentication = securityContext.getAuthentication();
            if (ObjectUtil.isNotNull(authentication)) {
                Object object = authentication.getDetails();
                if (ObjectUtil.isNotNull(object)) {
                    if (object instanceof SecurityUserInfo) {
                        return (SecurityUserInfo) object;
                    }
                }
            }
        }
        throw new RuntimeException("请从正确的入口进入系统");
    }

    public static void setUserDetails(SecurityUserInfo userInfo) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userInfo, null, userInfo.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(userInfo);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }


}
