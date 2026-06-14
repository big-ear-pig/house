package org.bigearpig.sso.module.dic.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import org.bigearpig.base.mybatis.MyBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_dic_item")
public class DicItemEntity extends MyBaseEntity {
	// t_dic的table_id
    private Long dicId;
    // 字典编码
    private String itemCode = "";
    // 字典页面展示
    private String itemValue = "";
    // 是否展示
    private Boolean showFlag = true;
    // 是否可以选择
    private Boolean checkFlag = true;

}
