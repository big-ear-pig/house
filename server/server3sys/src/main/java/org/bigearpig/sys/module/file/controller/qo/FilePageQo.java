package org.bigearpig.sys.module.file.controller.qo;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bigearpig.base.mybatis.BaseQo;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = false)

public class FilePageQo extends BaseQo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long tableIdEq;

	private Long createByEq;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTimeLe;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTimeGe;

	private Long updateByEq;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateTimeLe;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateTimeGe;

	private Integer sortNumEq;

	private Integer sortNumLe;

	private Integer sortNumGe;

	private Boolean delFlagEq;

	private Long parentIdEq;

	private String fileNameLike;

}
