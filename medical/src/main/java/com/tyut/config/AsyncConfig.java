package com.tyut.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync//启动异步支持
@Configuration
public class AsyncConfig {


    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(10);
        // 最大线程数
        executor.setMaxPoolSize(20);
        // 队列的容量
        executor.setQueueCapacity(50);
        // 线程名字前缀
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }

}
