package org.bigearpig.base.mybatis;

import lombok.Data;

@Data
public class BaseOrderItem implements Comparable<BaseOrderItem> {
    /**
     * 需要进行排序的字段
     */
    private String column ="create_time" ;

    /**
     * 排列顺序，默认 desc
     */
    private Boolean asc =false;

    /**
     * 排序
     */
    private Integer sort = 0;

    @Override
    public int compareTo(BaseOrderItem other){
        // 按 sort 升序排列（数值小的在前）
        int sortCompare = this.sort.compareTo(other.sort);
        if (sortCompare != 0) {
            return sortCompare;
        }
        // 如果 sort 相同，按 column 字母顺序升序（可选）
        return this.column.compareTo(other.column);
    }

}
