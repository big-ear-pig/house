package org.bigearpig.sys.module.seq.service;



public interface SeqService {



    // 根据流水号code 获取对应流水号队列下一个流水号
    String getNextSeq(String code);

}
