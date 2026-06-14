package org.bigearpig.base.feign.client.sys.file.vo;

import lombok.Data;

@Data
public class DownloadFileVo {

    private String fileName;

    private String fileType;

    private Long fileByteSize;

    private byte[] fileByte;

    private String mimeType;

    private String fileUrl;
}
