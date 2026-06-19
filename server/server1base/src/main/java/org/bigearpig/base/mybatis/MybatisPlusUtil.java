package org.bigearpig.base.mybatis;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.stream.Collectors;

public class MybatisPlusUtil {

    public static <T, E extends BaseQo> Page<T> generatePage(E qo, Class<T> c) {
        Page<T> page = new Page<T>(qo.getCurrent(), qo.getSize());
        if (ObjectUtil.isNotEmpty(qo.getOrderItemList())) {
            List<OrderItem> list = qo.getOrderItemList().stream().sorted().map(baseOrderItem -> {
                OrderItem item = new OrderItem();
                //字符串 转 下划线
                item.setColumn(StringUtils.camelToUnderline(baseOrderItem.getColumn()));
                item.setAsc(baseOrderItem.getAsc());
                return item;
            }).collect(Collectors.toList());
            page.addOrder(list);
        }
        return page;
    }
}
