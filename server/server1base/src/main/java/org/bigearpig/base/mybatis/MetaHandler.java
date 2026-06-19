package org.bigearpig.base.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MetaHandler implements MetaObjectHandler {

    @Autowired
    private List<FieldFillHandler> handlers;

    @Override
    public void insertFill(MetaObject metaObject) {
        // 遍历并执行所有自定义的填充逻辑
        for (FieldFillHandler handler : handlers) {
            handler.insertFill(metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 遍历并执行所有自定义的填充逻辑
        for (FieldFillHandler handler : handlers) {
            handler.updateFill(metaObject);
        }
    }

}
