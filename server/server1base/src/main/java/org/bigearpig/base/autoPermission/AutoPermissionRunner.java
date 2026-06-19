package org.bigearpig.base.autoPermission;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.bigearpig.base.feign.client.sso.user.UserFeignClient;
import org.bigearpig.base.feign.client.sso.user.mo.AddPermissionInnerMo;
import org.bigearpig.base.feign.client.sso.user.vo.PermissionInnerVo;
import org.bigearpig.base.security.SecurityUserInfo;
import org.bigearpig.base.security.SecurityUtil;
import org.bigearpig.common.ResultData;
import org.bigearpig.common.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class AutoPermissionRunner implements ApplicationRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserFeignClient userFeignClient;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 放入用户信息
        String token = JSONObject.toJSONString(UserInfo.getNoAuthUser());
        SecurityUserInfo securityUserInfo = JSONObject.parseObject(token, SecurityUserInfo.class);
        SecurityUtil.setUserDetails(securityUserInfo);
        log.info("cglib 反射获取 permission");
        List<String> preAuthorizeExpressions = new ArrayList<>();
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RestController.class);
        for (Object bean : beans.values()) {
            Class<?> beanClass = bean.getClass();
            if (beanClass.getName().contains("CGLIB")) {
                Class<?> superClass = beanClass.getSuperclass();
                if (null != superClass && superClass != Object.class) {
                    Method[] methods = superClass.getDeclaredMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(PreAuthorize.class)) {
                            PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);
                            String expression = preAuthorize.value();
                            expression = expression.substring(17);
                            expression = expression.substring(0, expression.length() - 2);
                            preAuthorizeExpressions.add(expression);
                        }
                    }
                }
            }
        }
        for (String str : preAuthorizeExpressions) {
            try {
                String[] strArr = str.split(":");
                AddPermissionInnerMo addPermissionInnerMo = new AddPermissionInnerMo();
                addPermissionInnerMo.setPerName(str);
                addPermissionInnerMo.setCodeSys(strArr[0]);
                addPermissionInnerMo.setCodeModule(strArr[1]);
                addPermissionInnerMo.setCodeMethod(strArr[2]);
                addPermissionInnerMo.setPerDescription(str);
                ResultData<PermissionInnerVo> result = userFeignClient.addPermissionInner(addPermissionInnerMo);
                log.info(JSONObject.toJSONString(result));
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }

        }
    }
}
