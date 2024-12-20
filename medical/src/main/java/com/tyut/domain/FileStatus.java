package com.tyut.domain;

import lombok.Getter;

/**
 * 文件状态
 */

@Getter
public enum FileStatus {

    WAITING("待处理"),
    PROCESSING("处理中"),
    COMPLETED("已完成"),
    FAILED("失败");

    private String status;

    private FileStatus(String status) {
        this.status = status;
    }



}
