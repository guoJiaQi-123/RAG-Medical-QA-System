package com.tyut.controller;

import com.tyut.service.ChatAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * ai 控制器
 */
@RestController
public class ChatController {

    @Autowired
    private ChatAssistant chatAssistant;

    @GetMapping("/chat")
    public Flux<String> chat(String message){
        return chatAssistant.chat(message);
    }


}
