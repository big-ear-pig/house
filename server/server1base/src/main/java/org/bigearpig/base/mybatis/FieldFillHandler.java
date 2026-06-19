package org.bigearpig.base.mybatis;

import org.apache.ibatis.reflection.MetaObject;

import java.util.Objects;

public interface FieldFillHandler {
    // 插入时执行
    void insertFill(MetaObject metaObject);
    // 更新时执行
    void updateFill(MetaObject metaObject);

    default FieldFillHandler setFieldValByName(String fieldName, Object fieldVal, MetaObject metaObject) {
        if (Objects.nonNull(fieldVal) && metaObject.hasSetter(fieldName)) {
            metaObject.setValue(fieldName, fieldVal);
        }
        return this;
    }

    default Object getFieldValByName(String fieldName, MetaObject metaObject) {
        return metaObject.hasGetter(fieldName) ? metaObject.getValue(fieldName) : null;
    }
}