package org.bigearpig.sys.module.file.component;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;



import javax.annotation.Resource;
import java.io.InputStream;

@Slf4j
@Component
public class MinioComponent {

    @Value("${bigearpig.minio.defaultBucketName}")
    private String defaultBucketName;
    @Resource
    private MinioClient minioClient;
    @Resource
    private Snowflake snowflake;
    
    
    public MinioClient getMinioClient() {
    	return minioClient;
    }

    // 上传文件到一个文件桶中
    public String uploadFile(MultipartFile file) {
        return uploadFile(defaultBucketName, file);
    }

    // 上传文件到一个文件桶中
    private String uploadFile(String bucketName, MultipartFile file) {
        if (StrUtil.isBlank(bucketName)) {
            log.info("bucketName为空");
        }
        log.info(file.getContentType());
        String fileUuid = snowflake.nextIdStr() + file.getOriginalFilename();
        try {
            ObjectWriteResponse response = minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .object(fileUuid)
                    .contentType(file.getContentType())
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
        return fileUuid;
    }

    // 从一个文件桶中下载文件
    public InputStream downloadFile(String fileUuid) {
        return downloadFile(defaultBucketName, fileUuid);
    }

    // 从一个文件桶中下载文件
    private InputStream downloadFile(String bucketName, String fileUuid) {
        GetObjectResponse getObjectResponse = null;
        try {
            getObjectResponse = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileUuid)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
        return getObjectResponse;
    }
}
