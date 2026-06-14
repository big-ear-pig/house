package org.bigearpig.sys.module.file.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bigearpig.base.mybatis.MyBaseEntity;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_file")
public class FileEntity extends MyBaseEntity {
	// 文件名称
	private String fileName = "";
	// 文件大小
	private Long fileByteSize = 0L;
	// 文件地址
	private String fileUrl = "";
	// 文件类型
	private String fileType = "";
	// 流媒体地址
	private String m3u8Url = "";
	// 上传文件的功能编码
	private String ownName;

}
