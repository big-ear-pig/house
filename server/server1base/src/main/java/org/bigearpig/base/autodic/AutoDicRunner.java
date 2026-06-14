package org.bigearpig.base.autodic;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.bigearpig.base.feign.client.sso.dic.DicFeignClient;
import org.bigearpig.base.feign.client.sso.dic.mo.AddDicInnerMo;
import org.bigearpig.base.feign.client.sso.dic.mo.AddDicItemInnerMo;
import org.bigearpig.base.feign.client.sso.dic.vo.DicItemInnerVo;
import org.bigearpig.base.security.SecurityUserInfo;
import org.bigearpig.base.security.SecurityUtil;
import org.bigearpig.common.BaseConstant;
import org.bigearpig.common.ResultData;
import org.bigearpig.common.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;



@Slf4j
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class AutoDicRunner implements ApplicationRunner {
    @Autowired
    private DicFeignClient dicFeignClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String token = JSONObject.toJSONString(UserInfo.getNoAuthUser());
        SecurityUserInfo securityUserInfo = JSONObject.parseObject(token, SecurityUserInfo.class);
        SecurityUtil.setUserDetails(securityUserInfo);
        log.info("反射获取 dic ");
        String basePackage = BaseConstant.BASE_PACKAGE;
        String resourcePattern = "/**/*.class";
        try {
            //spring工具类，可以获取指定路径下的全部类
            PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    ClassUtils.convertClassNameToResourcePath(basePackage) + resourcePattern;
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory(resourcePatternResolver);


            for (Resource resource : resources) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                ClassMetadata classMetadata = metadataReader.getClassMetadata();
                if (classMetadata.isInterface() || classMetadata.isAbstract()) {
                    // 接口或抽象类，不处理
                    continue;
                }
                String[] interfaceNames = classMetadata.getInterfaceNames();

                for (String interfaceName : interfaceNames) {
                    if (interfaceName.equals(DicEnumInterface.class.getName())) {
                        String className = classMetadata.getClassName();


                        Class<?> clazz = (Class<?>) Class.forName(className);
                        String enclosingClassName = clazz.getSimpleName();
                        String dicCode = "";
                        if (StrUtil.isNotBlank(enclosingClassName)) {
                            dicCode = enclosingClassName.substring(0, 1).toLowerCase() + enclosingClassName.substring(1, enclosingClassName.length() - 4);
                        }
                        AddDicInnerMo addDicInnerMo = new AddDicInnerMo();
                        addDicInnerMo.setDicCode(dicCode);
                        addDicInnerMo.setDicName("枚举" + dicCode);
                        addDicInnerMo.setDicDescription(className);


                        for (Object obj : clazz.getEnumConstants()) {
                            if (obj instanceof DicEnumInterface) {
                                DicEnumInterface dicEnumInterface = (DicEnumInterface) obj;
                                AddDicItemInnerMo addDicItemInnerMo = new AddDicItemInnerMo();
                                addDicItemInnerMo.setItemCode(dicEnumInterface.getItemCode());
                                addDicItemInnerMo.setItemValue(dicEnumInterface.getItemValue());
                                addDicInnerMo.getItemList().add(addDicItemInnerMo);
                            }
                        }

                        ResultData<DicItemInnerVo> result= dicFeignClient.addDicInner(addDicInnerMo);
                        log.info(JSONObject.toJSONString(result));
                        break;
                    }
                }


            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


}
