package org.bigearpig.sys.module.file.service.bo;

import lombok.Data;

import java.io.InputStream;

@Data
public class FileBo {

    private String fileName;

    private Long fileSize;

    private InputStream inputStream;
}
