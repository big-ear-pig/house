package org.bigearpig.sys.module.seq.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;

import org.bigearpig.common.ResultData;
import org.bigearpig.sys.module.seq.controller.mo.SaveSeqMo;
import org.bigearpig.sys.module.seq.controller.qo.SeqPageQo;
import org.bigearpig.sys.module.seq.controller.vo.SeqControllerVo;
import org.bigearpig.sys.module.seq.service.SeqService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j

@RestController
@RequestMapping(value = "/seq")
@AllArgsConstructor
public class SeqController {

	private SeqService seqService;


	@PostMapping(value = "/getNextSeq")
	public ResultData<String> getNextSeq(@RequestParam String code) {
		return ResultData.setObj(seqService.getNextSeq(code));
	}


	@PostMapping(value = "/queryListSeq")
	public ResultData<List<SeqControllerVo>> queryListSeq(@RequestBody @Valid SeqPageQo seqPageQo) {
		return null;
	}


	@PostMapping(value = "/queryPageSeq")
	public ResultData<IPage<SeqControllerVo>> queryPageSeq(@RequestBody @Valid SeqPageQo seqPageQo) {
		return null;
	}


	@PostMapping(value = "/getSeqById")
	public ResultData<SeqControllerVo> getSeqById(@RequestParam Long id) {

		return null;
	}


	@PostMapping(value = "/saveSeq")
	public ResultData<SeqControllerVo> saveSeq(@RequestBody @Valid SaveSeqMo saveSeqMo) {
		return null;
	}
}
