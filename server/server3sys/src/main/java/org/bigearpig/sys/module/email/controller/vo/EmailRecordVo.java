package org.bigearpig.sys.module.email.controller.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)

public class EmailRecordVo  {

    private String addrFrom;

    private String addrTo;

    private String subject;

    private String content;

    private String fileIdList;

    private String sendStatus;

    private LocalDateTime sendTime;
}
