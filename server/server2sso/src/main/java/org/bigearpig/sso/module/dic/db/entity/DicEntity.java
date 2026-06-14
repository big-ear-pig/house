package org.bigearpig.sso.module.dic.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bigearpig.base.mybatis.MyBaseEntity;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_dic")
public class DicEntity extends MyBaseEntity {


    // 字典名字
    private String dicName = "";
    // 字典编码
    private String dicCode = "";
    // 描述
    private String dicDescription = "";
    // 字典类型  普通字典  枚举字典
    private String dicType;

}
