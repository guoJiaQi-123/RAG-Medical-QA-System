package com.tyut.service;

import reactor.core.publisher.Flux;

/**
 * AI 助手接口
 */
public interface ChatAssistant {


    /**
     * 会话
     * @param message
     * @return
     */
    Flux<String> chat(String message);


}
