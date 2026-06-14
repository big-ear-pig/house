package org.bigearpig.sso.module.dic.db.entity;


import org.bigearpig.base.autodic.DicEnumInterface;

public enum DicTypeEnum implements DicEnumInterface {
    UNKNOW("0","未知"),
    DICTYPE("1","数据字典"),
    SYSTEMENUM("2","系统枚举"),


    ;
    private final String itemCode;
    private final String itemValue;

    DicTypeEnum(String itemCode, String itemValue) {
        this.itemCode = itemCode;
        this.itemValue = itemValue;
    }

    @Override
    public String getItemCode() {
        return this.itemCode;
    }

    @Override
    public String getItemValue() {
        return this.itemValue;
    }
}
