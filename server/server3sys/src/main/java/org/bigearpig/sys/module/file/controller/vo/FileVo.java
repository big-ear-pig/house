package org.bigearpig.sys.module.file.controller.vo;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.InputStream;

@Data
@EqualsAndHashCode(callSuper = false)

public class FileVo {
    /**
     *
     */
    private static final long serialVersionUID = 1L;


    private String fileName;

    private String fileType;

    private Long fileByteSize;

    private byte[] fileByte;

    private String mimeType;

    private String fileUrl;

    private InputStream inputStream;

    private String ownName;

    // 文件上传来源地址
    private String uploadPath = "";
}
