package org.bigearpig.sso.module.dic.controller.mo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddDicMo {

    
    @NotBlank(message = "字典编码不能为空")
    private String dicCode;
    @NotBlank(message = "字典名称不能为空")
    private String dicName;
    
    private String dicDescription;


}
