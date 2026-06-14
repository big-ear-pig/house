package org.bigearpig.sys.module.file.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.bigearpig.sys.module.file.component.MinioComponent;
import org.bigearpig.sys.module.file.controller.qo.FilePageQo;
import org.bigearpig.sys.module.file.controller.vo.FileVo;
import org.bigearpig.sys.module.file.db.entity.FileEntity;
import org.bigearpig.sys.module.file.db.service.FileEntityService;
import org.bigearpig.sys.module.file.service.FileService;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    @Resource
    private MinioComponent minioComponent;
    @Resource
    private FileEntityService fileEntityService;
    @Value("${bigearpig.minio.defaultBucketName}")
    private String defaultBucketName;

    @Override
    public Long uploadFile(MultipartFile file) {
        String fileUrl = minioComponent.uploadFile(file);
        String fileName = file.getOriginalFilename();
        String fileType = FileUtil.getSuffix(fileName);
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(fileName);
        fileEntity.setFileType(fileType);
        fileEntity.setFileByteSize(file.getSize());
        fileEntity.setFileUrl(fileUrl);
        fileEntityService.save(fileEntity);
        return fileEntity.getTableId();
    }

    @Async("taskExecutor")
    @Override
    public void mp4ToM3u8(FileEntity fileEntity, MultipartFile file) {
        // TODO Auto-generated method stub

    }


    @Override
    public FileVo downloadFile(Long id) throws IOException {
        FileEntity fileEntity = fileEntityService.getById(id);
        if (ObjectUtil.isNull(fileEntity)) {
            throw new RuntimeException("找不到对应的数据");
        }
        InputStream inStream = minioComponent.downloadFile(fileEntity.getFileUrl());
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        FileVo vo = new FileVo();
        vo.setFileName(fileEntity.getFileName());
        vo.setFileByte(outStream.toByteArray());
        vo.setFileByteSize((long) outStream.size());
        vo.setMimeType(FileUtil.getMimeType(fileEntity.getFileName()));
        return vo;
    }


    @Override
    public IPage<FileVo> queryPageFile(FilePageQo filePageQo) {
        Page<FileEntity> page = null;
        QueryWrapper<FileEntity> queryWrapper = null;

        page = fileEntityService.page(page, queryWrapper);
        return page.convert(fileEntity -> {
            FileVo fileVo = new FileVo();
            BeanUtil.copyProperties(fileEntity, fileVo);
            return fileVo;
        });
    }

	@Override
	public FileVo getFileById(Long id) {
		FileEntity fileEntity = fileEntityService.getById(id);
		FileVo vo =new FileVo();
		BeanUtil.copyProperties(fileEntity,vo);
		return vo;
	}

}