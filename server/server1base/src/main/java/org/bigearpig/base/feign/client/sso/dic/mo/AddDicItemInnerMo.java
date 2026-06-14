package org.bigearpig.base.feign.client.sso.dic.mo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddDicItemInnerMo {


    private Long dicId;
    @NotBlank(message = "")
    private String itemCode;
    @NotBlank(message = "")
    private String itemValue;

    // 是否展示
    private Boolean showFlag = true;
    // 是否可以选择
    private Boolean checkFlag = true;

}
