package com.tyut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tyut.domain.FileInfo;
import com.tyut.mapper.FileInfoMapper;
import com.tyut.service.FileInfoService;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【file_info】的数据库操作Service实现
* @createDate 2024-11-26 14:03:08
*/
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo>
    implements FileInfoService {

}




