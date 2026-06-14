package org.bigearpig.sys.module.seq.controller.qo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bigearpig.base.mybatis.BaseQo;

@Data
@EqualsAndHashCode(callSuper = false)

public class SeqPageQo extends BaseQo {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String name;

}
