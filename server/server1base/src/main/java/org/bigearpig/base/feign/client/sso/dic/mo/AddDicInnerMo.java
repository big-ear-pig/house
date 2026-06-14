package org.bigearpig.base.feign.client.sso.dic.mo;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
public class AddDicInnerMo {
    @NotBlank(message = "字典编码不能为空")
    private String dicCode;

    private String dicName;

    private String dicDescription;
    @NotEmpty
    private List<AddDicItemInnerMo> itemList = new ArrayList<>();
}
