package org.bigearpig.sys.module.seq.db.entity;


import org.bigearpig.base.autodic.DicEnumInterface;

public enum RebornTypeEnum implements DicEnumInterface {
    UNKNOW("0","未知"),
    DAYREBORN("1","日流水号"),
    MONTHREBORN("2","月流水号"),
    YEARREBORN("3","年流水号"),
    ALLREBORN("4","不限时流水号"),

    ;
    private final String itemCode;
    private final String itemValue;

    RebornTypeEnum(String itemCode, String itemValue) {
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
