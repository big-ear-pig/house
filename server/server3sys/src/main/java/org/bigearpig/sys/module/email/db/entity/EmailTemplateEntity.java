package org.bigearpig.sys.module.email.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bigearpig.base.mybatis.MyBaseEntity;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_email_template")
public class EmailTemplateEntity extends MyBaseEntity {

	private String emailTemplateCode;
	// email的标题
	private String subject;

	 private String content;
}
