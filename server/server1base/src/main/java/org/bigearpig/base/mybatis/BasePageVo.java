package org.bigearpig.base.mybatis;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class BasePageVo<T> implements Serializable {

    private List<T> records = Collections.emptyList();
    private long total = 0;
    private long size = 10;
    private long current = 1;
    private List<BaseOrderItem> orderItemList=new ArrayList<>();

    public BasePageVo(){

    }

    private <M extends BaseQo> BasePageVo(M baseQo) {
        this.current = baseQo.getCurrent();
        this.size = baseQo.getSize();
        this.orderItemList = baseQo.getOrderItemList();
    }
    /**
     * 自定义查询返回page转vo
     * @param <T>
     * @param <M>
     * @param baseQo
     * @param total
     * @param records
     * @return
     */
    public static <T,M extends BaseQo> BasePageVo<T> build(M baseQo,long total,List<T> records){
        BasePageVo<T> result = new BasePageVo<>(baseQo);
        result.setTotal(total);
        result.setRecords(records);
        return result;
    }
    /**
     * 单表查询 转换成 vo返回 
     * @param <R>
     * @param vo
     * @param mapper
     * @return
     */
    public static <R> BasePageVo<R> convert(BasePageVo vo,Function mapper) {
        List<R> collect = (List)vo.getRecords().stream().map(mapper).collect(Collectors.toList());
        vo.setRecords(collect);
        return vo;
    }

}
