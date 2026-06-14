package org.bigearpig.sys.module.file.controller.qo;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bigearpig.base.mybatis.BaseQo;


@Data
@EqualsAndHashCode(callSuper = false)

public class FilePageQo extends BaseQo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fileNameLike;

}
