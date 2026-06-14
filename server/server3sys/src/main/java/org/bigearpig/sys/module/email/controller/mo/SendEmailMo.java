package org.bigearpig.sys.module.email.controller.mo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class SendEmailMo {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    private String to;

    private String subject;

    private String content;

    private List<Long> imgList;

    private List<Long> fileList;


}
