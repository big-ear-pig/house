package org.bigearpig.sys.module.seq.controller.vo;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

public class SeqControllerVo {
    /**
     *
     */
    private static final long serialVersionUID = 1L;



    private String name;

    private String lastSeq;
}
