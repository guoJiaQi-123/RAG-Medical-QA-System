package com.tyut;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 医疗问答主启动类
 */
@Slf4j
@EnableScheduling//启动定时任务
@MapperScan("com.tyut.mapper")
@SpringBootApplication
public class MedicalApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedicalApplication.class, args);
        log.info("********** 医疗问答系统启动成功 *********** ");
    }
}
