package org.bigearpig.sys.module.file.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.multipart.MultipartFile;
import org.bigearpig.sys.module.file.controller.qo.FilePageQo;
import org.bigearpig.sys.module.file.controller.vo.FileVo;
import org.bigearpig.sys.module.file.db.entity.FileEntity;

import java.io.IOException;

public interface FileService {

    Long uploadFile(MultipartFile file);
    
    void mp4ToM3u8(FileEntity fileEntity, MultipartFile file);


    FileVo downloadFile(Long id) throws IOException;



    IPage<FileVo> queryPageFile(FilePageQo filePageQo);

    FileVo getFileById(Long id);


}