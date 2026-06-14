package org.bigearpig.sys.module.email.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import org.bigearpig.base.mybatis.MyBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_email_template_param")
public class EmailTemplateParamEntity  extends MyBaseEntity {

	private Long emailTemplateId;
	
	private String paramLabel;
}
