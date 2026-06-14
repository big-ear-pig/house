package org.bigearpig.base.mybatis;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.bigearpig.base.security.SecurityUserInfo;
import org.bigearpig.base.security.SecurityUtil;

import java.time.LocalDateTime;

@Slf4j
public class MetaHandler implements MetaObjectHandler {
	@Override
	public void insertFill(MetaObject metaObject) {
		// 获取登录用户id
		SecurityUserInfo userInfo = SecurityUtil.getUserDetails();
		if (ObjectUtil.isNull(getFieldValByName("createBy", metaObject))) {
			this.setFieldValByName("createBy", userInfo.getTableId(), metaObject);
		}
		if (ObjectUtil.isNull(getFieldValByName("createTime", metaObject))) {
			this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
		}
		if (ObjectUtil.isNull(getFieldValByName("updateBy", metaObject))) {
			this.setFieldValByName("updateBy", userInfo.getTableId(), metaObject);
		}
		if (ObjectUtil.isNull(getFieldValByName("updateTime", metaObject))) {
			this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
		}
		if (ObjectUtil.isNull(getFieldValByName("sortNum", metaObject))) {
			this.setFieldValByName("sortNum", 0, metaObject);
		}
		if (ObjectUtil.isNull(getFieldValByName("delFlag", metaObject))) {
			this.setFieldValByName("delFlag", false, metaObject);
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		// 获取登录用户id
		SecurityUserInfo userInfo = SecurityUtil.getUserDetails();
		if (ObjectUtil.isNull(getFieldValByName("updateBy", metaObject))) {
			this.setFieldValByName("updateBy", userInfo.getTableId(), metaObject);
		}
		if (ObjectUtil.isNull(getFieldValByName("updateTime", metaObject))) {
			this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
		}

	}

}
