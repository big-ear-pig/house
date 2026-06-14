package org.bigearpig.base.mybatis;

import lombok.Data;

@Data
public class BaseOrderItem {
    /**
     * 需要进行排序的字段
     */
    private String column ="create_time" ;

    /**
     * 排列顺序，默认 desc
     */
    private Boolean asc =false;

}
