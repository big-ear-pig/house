package org.bigearpig.sys.module.seq.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import cn.hutool.core.util.StrUtil;
import org.bigearpig.sys.module.seq.db.entity.RebornTypeEnum;
import org.bigearpig.sys.module.seq.mapper.SeqMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.bigearpig.sys.module.seq.db.entity.SeqEntity;
import org.bigearpig.sys.module.seq.db.service.SeqEntityService;
import org.bigearpig.sys.module.seq.service.SeqService;
import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;


@Slf4j
@Service
@AllArgsConstructor
public class SeqServiceImpl implements SeqService {

    private StringRedisTemplate stringRedisTemplate;

    private SeqEntityService seqEntityService;

    private SeqMapper seqMapper;





    @Override
    public String getNextSeq(String code) {
        QueryWrapper<SeqEntity> queryWrapper = new QueryWrapper<SeqEntity>();
        queryWrapper.lambda().eq(SeqEntity::getCode, code);
        SeqEntity seqEntity = seqEntityService.getOne(queryWrapper);
        if (ObjectUtil.isNull(seqEntity)) {
            throw new RuntimeException("");
        }
        // 获取流水号num
        String timeCode = "";
        String timeFormat = "";
        SimpleDateFormat sdf = new SimpleDateFormat();
        if (StrUtil.equals(RebornTypeEnum.DAYREBORN.getItemCode(), seqEntity.getRebornType())) {
            timeFormat = "yyyyMMdd";
            sdf.applyPattern(timeFormat);
            timeCode = sdf.format(new Date());
        } else if (StrUtil.equals(RebornTypeEnum.MONTHREBORN.getItemCode(), seqEntity.getRebornType())) {
            timeFormat = "yyyyMM";
            sdf.applyPattern(timeFormat);
            timeCode = sdf.format(new Date());
        } else if (StrUtil.equals(RebornTypeEnum.YEARREBORN.getItemCode(), seqEntity.getRebornType())) {
            timeFormat = "yyyy";
            sdf.applyPattern(timeFormat);
            timeCode = sdf.format(new Date());
        } else if (StrUtil.equals(RebornTypeEnum.ALLREBORN.getItemCode(), seqEntity.getRebornType())) {

        } else {

        }

        String key = "" + timeCode + code;
        Long redisNum = stringRedisTemplate.opsForValue().increment(key);
        if (null != redisNum) {
            if (redisNum == 1L) {
                if (StrUtil.equals(RebornTypeEnum.DAYREBORN.getItemCode(), seqEntity.getRebornType())) {
                    stringRedisTemplate.expire(key, 1, TimeUnit.DAYS);
                } else if (StrUtil.equals(RebornTypeEnum.MONTHREBORN.getItemCode(), seqEntity.getRebornType())) {
                    stringRedisTemplate.expire(key, 31, TimeUnit.DAYS);
                } else if (StrUtil.equals(RebornTypeEnum.YEARREBORN.getItemCode(), seqEntity.getRebornType())) {
                    stringRedisTemplate.expire(key, 366, TimeUnit.DAYS);
                } else if (StrUtil.equals(RebornTypeEnum.ALLREBORN.getItemCode(), seqEntity.getRebornType())) {

                } else {

                }

            }
        }
        // 将num补全位数
        String Longformat = "";
        int size = String.valueOf(redisNum).length();
        if (size < seqEntity.getSupplementNum()) {
            String format = "%" + seqEntity.getSupplementFlag() + seqEntity.getSupplementNum() + "d";
            Longformat = String.format(format, redisNum);
        }
        return seqEntity.getPrefix() + timeCode + Longformat + seqEntity.getSuffix();
    }


}
