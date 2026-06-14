package org.bigearpig.sso.module.dic.controller.qo;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bigearpig.base.mybatis.BaseQo;


@Data
@EqualsAndHashCode(callSuper = true)

public class DicItemQo extends BaseQo {

    private Long dicId;



}
