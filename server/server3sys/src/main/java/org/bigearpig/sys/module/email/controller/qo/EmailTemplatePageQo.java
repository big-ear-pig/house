package org.bigearpig.sys.module.email.controller.qo;




import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bigearpig.base.mybatis.BaseQo;

@Data
@EqualsAndHashCode(callSuper = false)

public class EmailTemplatePageQo extends BaseQo {

    private String name;
}
