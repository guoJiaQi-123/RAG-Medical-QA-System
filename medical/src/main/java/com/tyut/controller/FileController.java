package com.tyut.controller;

import com.tyut.common.ResponseResult;
import com.tyut.domain.FileInfo;
import com.tyut.domain.FileStatus;
import com.tyut.service.FileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 知识库控制器
 */
@RestController
public class FileController {

    /**
     * 文件路径
     */
    @Value("${file.path}")
    private String filePath;

    @Autowired
    private FileInfoService fileInfoService;

    /**
     * 获取知识体列表信息
     * @return
     */
    @GetMapping("/getList")
    public List<FileInfo> getList(){
        return fileInfoService.list();
    }

    /**
     * 知识库上传
     * @param file
     */
    @PostMapping("/upload")
    public ResponseResult<Boolean> upload(MultipartFile file) throws IOException {

        // 文件名字
        String originalFilename = file.getOriginalFilename();
        // 文件大小
        long filesize = file.getSize();
        // 获取文件字符
        int charCount = getCharCount(file);
        // 分段
        int segments = getSegments(filesize);
        // 需要保存到数据库
        FileInfo fileInfo = new FileInfo();
        // 文件名字
        fileInfo.setFileName(originalFilename);
        // 文件大小
        fileInfo.setFileSize(filesize);
        // 文件字符
        fileInfo.setCharCount(charCount);
        // 文件分段
        fileInfo.setSegments(segments);
        // 文件状态
        fileInfo.setStatus(FileStatus.WAITING.getStatus());
        // 文件上传时间
        fileInfo.setUploadTime(LocalDateTime.now());
        File medicalFile = new File(filePath + originalFilename);
        // 将文件保存到目标文件
        file.transferTo(medicalFile);
        fileInfo.setFilePath(filePath + originalFilename);
        boolean save = fileInfoService.save(fileInfo);
        if (save) {
            return ResponseResult.success(true);
        }else {
            return ResponseResult.fail("服务异常");
        }
    }


    /**
     * 获取文件字符数量
     * @param file
     * @return
     */
    private int getCharCount(MultipartFile file) throws IOException {
        String content = new String(file.getBytes());
        return content.length();
    }

    /**
     * 根据文件大小计算分段  5MB
     * @param filesize
     * @return
     */
    private int getSegments(long filesize){
        // 5MB
        long size  = 5 * 1024 * 1024;
        return (int)Math.ceil((double) (filesize / size));

    }


}
