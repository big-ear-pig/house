package org.bigearpig.base.feign.client.sys.file;


import org.bigearpig.base.feign.client.sys.file.vo.DownloadFileVo;
import org.bigearpig.common.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@FeignClient(name = "server3sys", contextId = "file", path = "/file")
public interface FileFeignClient {

    @PostMapping(value = "/uploadFileInner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResultData<String> uploadFileInner(@RequestPart(value = "file", required = true) MultipartFile file);

    @PostMapping(value = "/downloadFileInner")
    ResultData<DownloadFileVo> downloadFileInner(@RequestBody String id) throws IOException;
}
