package org.bigearpig.sso.module.dic.controller.mo;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UpdateDicMo {


    private String dicName;

    private String dicDescription;
}
