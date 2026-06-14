package org.bigearpig.sys.module.file.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.bigearpig.base.feign.Inner;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.bigearpig.common.ResultData;
import org.bigearpig.sys.module.file.controller.mo.FileMo;
import org.bigearpig.sys.module.file.controller.qo.FilePageQo;
import org.bigearpig.sys.module.file.controller.vo.FileVo;
import org.bigearpig.sys.module.file.service.FileService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;

@Slf4j

@RestController
@RequestMapping(value = "/file")
public class FileController {
	@Resource
	private FileService fileService;


	@PostMapping(value = "/uploadFile")
	public ResultData<String> uploadFile(MultipartFile file) {
		Long result = fileService.uploadFile(file);
		return ResultData.setObj(result+"");
	}

	@Inner
	@PostMapping(value = "/uploadFileInner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResultData<String> uploadFileInner(@RequestPart(value = "file", required = true) MultipartFile file){
		Long result = fileService.uploadFile(file);
		return ResultData.setObj(result+"");
	};



	@PostMapping(value = "/downloadFileInner")
	public ResultData<FileVo> downloadFile(@RequestBody String id) throws IOException {
		FileVo result = fileService.downloadFile(Long.valueOf(id));
		return ResultData.setObj(result);
	}


	@PostMapping(value = "/queryPageFile")
	@PreAuthorize(value = "hasAnyAuthority('sys:file:queryPageFile')")
	public ResultData<IPage<FileVo>> queryPageFile(@RequestBody @Valid FilePageQo filePageQo) {
		return ResultData.setObj(fileService.queryPageFile(filePageQo));
	}

	@PostMapping(value = "/updateOwnNameInner")
	public void updateOwnNameInner(@RequestBody FileMo mo){

	}


}
