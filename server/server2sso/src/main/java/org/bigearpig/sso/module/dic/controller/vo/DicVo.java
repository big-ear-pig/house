package org.bigearpig.sso.module.dic.controller.vo;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
@Data
@EqualsAndHashCode(callSuper = false)
public class DicVo  {

    // 字典名字
    private String dicName = "";
    // 字典编码
    private String dicCode = "";
    // 描述
    private String dicDescription = "";

    private String dicType;

    private List<DicItemVo> dicItemVoList;

}
