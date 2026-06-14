package org.bigearpig.base.feign.client.sys.email.vo;

import lombok.Data;

import java.util.Date;

@Data
public class EmailRecordInnerVo {

    private String addrTo;

    private String subject;

    private String content;

    private Boolean sendStatus;

    private Date sendTime;
}
