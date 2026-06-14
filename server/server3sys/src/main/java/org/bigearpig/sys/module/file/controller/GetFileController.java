package org.bigearpig.sys.module.file.controller;

import cn.hutool.core.util.StrUtil;
import io.minio.GetObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.bigearpig.sys.module.file.component.MinioComponent;
import org.bigearpig.sys.module.file.controller.vo.FileVo;
import org.bigearpig.sys.module.file.service.FileService;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

@Slf4j
@Controller
@RequestMapping(value = "/getFile")
public class GetFileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private MinioComponent minioComponent;

    @Value("${bigearpig.minio.defaultBucketName}")
    private String defaultBucketName;

    @GetMapping("/getVideoFile/{id}/{fileToken}")
    public ResponseEntity<StreamingResponseBody> getVideoFile(
            @PathVariable String id,
            @PathVariable String fileToken,
            @RequestHeader(value = HttpHeaders.RANGE, required = false) String rangeHeader,
            HttpServletRequest request) {

        try {
            log.info("视频文件请求: id={}, token={}, range={}", id, fileToken, rangeHeader);

            // 1. 验证token（需要实现）
            // if (!validateToken(fileToken, id, "video")) {
            //     return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            // }

            // 2. 获取文件信息
            FileVo fileVo = fileService.getFileById(Long.valueOf(id));
            if (fileVo == null) {
                log.warn("文件不存在: id={}", id);
                return ResponseEntity.notFound().build();
            }

            // 3. 获取MinIO文件信息
            StatObjectResponse stat = getFileStat(fileVo);
            long fileSize = stat.size();
            String contentType = getVideoContentType(fileVo, stat);

            // 4. 处理Range请求
            if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                return handleExplicitRange(fileVo, rangeHeader, fileSize, contentType);
            } else {
                return handleImplicitRange(fileVo, fileSize, contentType);
            }

        } catch (NumberFormatException e) {
            log.error("ID格式错误: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("视频流传输失败: id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ============= 辅助方法 =============

    /**
     * 获取文件统计信息
     */
    private StatObjectResponse getFileStat(FileVo fileVo) throws Exception {
        return minioComponent.getMinioClient().statObject(
                StatObjectArgs.builder()
                        .bucket(defaultBucketName)
                        .object(fileVo.getFileUrl())
                        .build()
        );
    }

    /**
     * 获取指定范围的输入流 - 🔥 新增的关键方法
     */
    private InputStream getRangeInputStream(String fileUuid, long offset, long length) {
        try {
            return minioComponent.getMinioClient().getObject(
                    GetObjectArgs.builder()
                            .bucket(defaultBucketName)
                            .object(fileUuid)
                            .offset(offset)      // 起始位置
                            .length(length)      // 读取长度
                            .build()
            );
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    /**
     * 获取完整文件的输入流
     */
    private InputStream getFullInputStream(String fileUuid) {
        try {
            return minioComponent.getMinioClient().getObject(
                    GetObjectArgs.builder()
                            .bucket(defaultBucketName)
                            .object(fileUuid)
                            .build());
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    /**
     * 获取视频Content-Type
     */
    private String getVideoContentType(FileVo fileVo, StatObjectResponse stat) {
        String contentType = stat.contentType();
        if (StrUtil.isBlank(contentType) || "application/octet-stream".equals(contentType)) {
            String fileName = fileVo.getFileName().toLowerCase();
            if (fileName.endsWith(".mp4")) {
                return "video/mp4";
            } else if (fileName.endsWith(".avi")) {
                return "video/x-msvideo";
            } else if (fileName.endsWith(".mov")) {
                return "video/quicktime";
            } else if (fileName.endsWith(".mkv")) {
                return "video/x-matroska";
            } else if (fileName.endsWith(".webm")) {
                return "video/webm";
            } else if (fileName.endsWith(".flv")) {
                return "video/x-flv";
            }
        }
        return contentType != null ? contentType : "video/mp4";
    }

    /**
     * 处理明确的Range请求（用户拖动）
     */
    private ResponseEntity<StreamingResponseBody> handleExplicitRange(
            FileVo fileVo, String rangeHeader,
            long fileSize, String contentType) throws Exception {

        String[] ranges = rangeHeader.substring(6).split("-");
        long rangeStart = Long.parseLong(ranges[0]);
        long rangeEnd = ranges.length > 1 ? Long.parseLong(ranges[1]) : fileSize - 1;

        if (rangeEnd >= fileSize) {
            rangeEnd = fileSize - 1;
        }

        long rangeLength = rangeEnd - rangeStart + 1;

        log.debug("处理明确Range请求: {}-{} (长度: {}字节)", rangeStart, rangeEnd, rangeLength);

        return buildRangeResponse(fileVo, rangeStart, rangeEnd, fileSize, contentType);
    }

    /**
     * 处理隐式Range请求（首次访问）
     */
    private ResponseEntity<StreamingResponseBody> handleImplicitRange(
            FileVo fileVo, long fileSize, String contentType) throws Exception {

        long rangeStart = 0;
        long rangeEnd;

        if (fileSize > 10 * 1024 * 1024) { // 大于10MB
            // 大文件：只返回前2MB
            rangeEnd = Math.min(2 * 1024 * 1024 - 1, fileSize - 1);
            log.debug("大文件({}MB)，返回前2MB", fileSize / (1024 * 1024));
        } else {
            // 小文件：返回完整内容
            rangeEnd = fileSize - 1;
            log.debug("小文件({}KB)，返回完整内容", fileSize / 1024);
        }

        long rangeLength = rangeEnd - rangeStart + 1;

        log.debug("处理隐式Range请求: {}-{} (长度: {}字节)", rangeStart, rangeEnd, rangeLength);

        return buildRangeResponse(fileVo, rangeStart, rangeEnd, fileSize, contentType);
    }

    /**
     * 构建Range响应（统一处理）
     */
    private ResponseEntity<StreamingResponseBody> buildRangeResponse(
            FileVo fileVo, long rangeStart, long rangeEnd,
            long fileSize, String contentType) throws Exception {

        long rangeLength = rangeEnd - rangeStart + 1;

        StreamingResponseBody stream = outputStream -> {
            try (InputStream inputStream = getRangeInputStream(
                    fileVo.getFileUrl(), rangeStart, rangeLength)) {
                IOUtils.copyLarge(inputStream, outputStream);
            } catch (Exception e) {
                log.error("流式传输异常", e);

            }
        };

        // 关键：总是返回206 Partial Content
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + rangeStart + "-" + rangeEnd + "/" + fileSize)
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(rangeLength))
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.EXPIRES, "0")
                .body(stream);
    }

    // ============= 其他方法（图片、音频、通用文件） =============

    @GetMapping("/getImageFile/{id}")
    public ResponseEntity<StreamingResponseBody> getImageFile(@PathVariable String id) {
        try {
            FileVo fileVo = fileService.getFileById(Long.valueOf(id));
            if (fileVo == null) {
                return ResponseEntity.notFound().build();
            }

            String contentType = getImageContentType(fileVo);

            StreamingResponseBody stream = outputStream -> {
                try (InputStream inputStream = getFullInputStream(fileVo.getFileUrl())) {
                    IOUtils.copyLarge(inputStream, outputStream);
                }
            };

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000") // 缓存1年
                    .body(stream);

        } catch (Exception e) {
            log.error("获取图片失败: id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取图片Content-Type
     */
    private String getImageContentType(FileVo fileVo) {
        String fileName = fileVo.getFileName().toLowerCase();
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".webp")) {
            return "image/webp";
        } else if (fileName.endsWith(".svg")) {
            return "image/svg+xml";
        } else if (fileName.endsWith(".bmp")) {
            return "image/bmp";
        }
        return "image/jpeg";
    }

    @GetMapping("/getMusicFile/{id}/{fileToken}")
    public ResponseEntity<StreamingResponseBody> getMusicFile(
            @PathVariable String id,
            @PathVariable String fileToken,
            @RequestHeader(value = HttpHeaders.RANGE, required = false) String rangeHeader) {

        try {
            // 验证token（需要实现）
            // if (!validateToken(fileToken, id, "audio")) {
            //     return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            // }

            FileVo fileVo = fileService.getFileById(Long.valueOf(id));
            if (fileVo == null) {
                return ResponseEntity.notFound().build();
            }

            StatObjectResponse stat = getFileStat(fileVo);
            long fileSize = stat.size();
            String contentType = getAudioContentType(fileVo, stat);

            // 处理Range请求
            if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                String[] ranges = rangeHeader.substring(6).split("-");
                long rangeStart = Long.parseLong(ranges[0]);
                long rangeEnd = ranges.length > 1 ? Long.parseLong(ranges[1]) : fileSize - 1;

                if (rangeEnd >= fileSize) {
                    rangeEnd = fileSize - 1;
                }

                long rangeLength = rangeEnd - rangeStart + 1;

                StreamingResponseBody stream = outputStream -> {
                    try (InputStream inputStream = getRangeInputStream(
                            fileVo.getFileUrl(), rangeStart, rangeLength)) {
                        IOUtils.copyLarge(inputStream, outputStream);
                    }
                };

                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_RANGE, "bytes " + rangeStart + "-" + rangeEnd + "/" + fileSize)
                        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(rangeLength))
                        .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                        .body(stream);
            }

            // 完整文件
            StreamingResponseBody stream = outputStream -> {
                try (InputStream inputStream = getFullInputStream(fileVo.getFileUrl())) {
                    IOUtils.copyLarge(inputStream, outputStream);
                }
            };

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileSize))
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .body(stream);

        } catch (Exception e) {
            log.error("获取音频文件失败: id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取音频Content-Type
     */
    private String getAudioContentType(FileVo fileVo, StatObjectResponse stat) {
        String contentType = stat.contentType();
        if (StrUtil.isBlank(contentType) || "application/octet-stream".equals(contentType)) {
            String fileName = fileVo.getFileName().toLowerCase();
            if (fileName.endsWith(".mp3")) {
                return "audio/mpeg";
            } else if (fileName.endsWith(".wav")) {
                return "audio/wav";
            } else if (fileName.endsWith(".flac")) {
                return "audio/flac";
            } else if (fileName.endsWith(".aac")) {
                return "audio/aac";
            } else if (fileName.endsWith(".ogg")) {
                return "audio/ogg";
            } else if (fileName.endsWith(".m4a")) {
                return "audio/mp4";
            }
        }
        return contentType != null ? contentType : "audio/mpeg";
    }

    @GetMapping("/getFile/{id}")
    public ResponseEntity<StreamingResponseBody> getFile(@PathVariable String id) {
        try {
            FileVo fileVo = fileService.getFileById(Long.valueOf(id));
            if (fileVo == null) {
                return ResponseEntity.notFound().build();
            }

            // 获取文件信息以确定Content-Type
            StatObjectResponse stat = getFileStat(fileVo);
            String contentType = getGenericContentType(fileVo, stat);

            StreamingResponseBody stream = outputStream -> {
                try (InputStream inputStream = getFullInputStream(fileVo.getFileUrl())) {
                    IOUtils.copyLarge(inputStream, outputStream);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileVo.getFileName() + "\"")
                    .body(stream);

        } catch (Exception e) {
            log.error("下载文件失败: id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取通用Content-Type
     */
    private String getGenericContentType(FileVo fileVo, StatObjectResponse stat) {
        String contentType = stat.contentType();
        if (StrUtil.isBlank(contentType) || "application/octet-stream".equals(contentType)) {
            String fileName = fileVo.getFileName().toLowerCase();
            if (fileName.endsWith(".pdf")) {
                return "application/pdf";
            } else if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
                return "application/msword";
            } else if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                return "application/vnd.ms-excel";
            } else if (fileName.endsWith(".ppt") || fileName.endsWith(".pptx")) {
                return "application/vnd.ms-powerpoint";
            } else if (fileName.endsWith(".zip")) {
                return "application/zip";
            } else if (fileName.endsWith(".rar")) {
                return "application/x-rar-compressed";
            } else if (fileName.endsWith(".txt")) {
                return "text/plain";
            } else if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
                return "text/html";
            } else if (fileName.endsWith(".json")) {
                return "application/json";
            } else if (fileName.endsWith(".xml")) {
                return "application/xml";
            }
        }
        return contentType != null ? contentType : "application/octet-stream";
    }

    /**
     * Token验证方法（需要你实现具体逻辑）
     */
    private boolean validateToken(String token, String fileId, String fileType) {
        // TODO: 实现你的token验证逻辑
        // 示例：
        // 1. 检查Redis中是否存在该token
        // 2. 验证token是否过期
        // 3. 验证token是否对应正确的文件ID和类型
        // 4. 验证成功后从Redis中删除token（实现单次使用）

        // 临时返回true用于测试
        log.debug("Token验证: fileId={}, type={}, token={}", fileId, fileType, token);
        return true;
    }
}