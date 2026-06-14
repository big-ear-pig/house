package org.bigearpig.base.feign;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.bigearpig.base.security.SecurityUserInfo;
import org.bigearpig.base.security.SecurityUtil;
import org.bigearpig.common.BaseConstant;
import org.bigearpig.common.JwtUtil;
import org.slf4j.MDC;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Slf4j
@Configuration
@EnableFeignClients(basePackages = BaseConstant.BASE_PACKAGE)
public class CommonFeignConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
    	template.header("From", "Y");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // controller 调用的open feign
        if (ObjectUtil.isNotNull(attributes)) {
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (ObjectUtil.isNotEmpty(headerNames)) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    template.header(name, values);
                }
            }
            // 代码调用的 open feign
        }else{
            SecurityUserInfo securityUserInfo = SecurityUtil.getUserDetails();
            String token = JwtUtil.generateJwt(JSONObject.toJSONString(securityUserInfo));
            template.header(BaseConstant.BASE_TOKEN_KEY, token);
        }
    }
}
