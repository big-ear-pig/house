package org.bigearpig.sys.module.email.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bigearpig.base.mybatis.MyBaseEntity;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_email_record")
public class EmailRecordEntity extends MyBaseEntity {

    private String fromName;

    private String addrFrom;

    private String toName;

    private String addrTo;

    private String subject;

    private String content;

    private Boolean sendStatus;

    private LocalDateTime sendTime;

    private String fileIdList;
}
