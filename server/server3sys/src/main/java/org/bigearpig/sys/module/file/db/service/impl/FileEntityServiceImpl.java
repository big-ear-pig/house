package org.bigearpig.sys.module.file.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.bigearpig.sys.module.file.db.entity.FileEntity;
import org.bigearpig.sys.module.file.db.mapper.FileEntityMapper;
import org.bigearpig.sys.module.file.db.service.FileEntityService;

@Service
public class FileEntityServiceImpl  extends ServiceImpl<FileEntityMapper, FileEntity> implements FileEntityService{

}
