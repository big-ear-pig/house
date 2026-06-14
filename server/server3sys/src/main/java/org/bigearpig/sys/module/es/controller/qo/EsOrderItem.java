package org.bigearpig.sys.module.es.controller.qo;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class EsOrderItem {

    /**
     * 需要进行排序的字段
     */
    private String column ="create_time" ;

    /**
     * 排列顺序，默认 desc
     */
    private Boolean asc = true;



}
