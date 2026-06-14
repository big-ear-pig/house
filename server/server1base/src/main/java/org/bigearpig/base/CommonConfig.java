package org.bigearpig.base;

import lombok.extern.slf4j.Slf4j;
import org.bigearpig.common.BaseConstant;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan(basePackages = BaseConstant.BASE_PACKAGE)
public class CommonConfig {

    public CommonConfig() {
        log.info("CommonConfig start");
    }

}
